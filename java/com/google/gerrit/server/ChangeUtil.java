begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|comparingInt
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Ordering
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|BaseEncoding
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Change
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Ref
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ChangeUtil
specifier|public
class|class
name|ChangeUtil
block|{
DECL|field|TOPIC_MAX_LENGTH
specifier|public
specifier|static
specifier|final
name|int
name|TOPIC_MAX_LENGTH
init|=
literal|2048
decl_stmt|;
DECL|field|UUID_RANDOM
specifier|private
specifier|static
specifier|final
name|Random
name|UUID_RANDOM
init|=
operator|new
name|SecureRandom
argument_list|()
decl_stmt|;
DECL|field|UUID_ENCODING
specifier|private
specifier|static
specifier|final
name|BaseEncoding
name|UUID_ENCODING
init|=
name|BaseEncoding
operator|.
name|base16
argument_list|()
operator|.
name|lowerCase
argument_list|()
decl_stmt|;
DECL|field|SUBJECT_MAX_LENGTH
specifier|private
specifier|static
specifier|final
name|int
name|SUBJECT_MAX_LENGTH
init|=
literal|80
decl_stmt|;
DECL|field|SUBJECT_CROP_APPENDIX
specifier|private
specifier|static
specifier|final
name|String
name|SUBJECT_CROP_APPENDIX
init|=
literal|"..."
decl_stmt|;
DECL|field|SUBJECT_CROP_RANGE
specifier|private
specifier|static
specifier|final
name|int
name|SUBJECT_CROP_RANGE
init|=
literal|10
decl_stmt|;
DECL|field|PS_ID_ORDER
specifier|public
specifier|static
specifier|final
name|Ordering
argument_list|<
name|PatchSet
argument_list|>
name|PS_ID_ORDER
init|=
name|Ordering
operator|.
name|from
argument_list|(
name|comparingInt
argument_list|(
name|PatchSet
operator|::
name|getPatchSetId
argument_list|)
argument_list|)
decl_stmt|;
DECL|method|formatChangeUrl (String canonicalWebUrl, Change change)
specifier|public
specifier|static
name|String
name|formatChangeUrl
parameter_list|(
name|String
name|canonicalWebUrl
parameter_list|,
name|Change
name|change
parameter_list|)
block|{
return|return
name|canonicalWebUrl
operator|+
literal|"c/"
operator|+
name|change
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"/+/"
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
return|;
block|}
comment|/** @return a new unique identifier for change message entities. */
DECL|method|messageUuid ()
specifier|public
specifier|static
name|String
name|messageUuid
parameter_list|()
block|{
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|8
index|]
decl_stmt|;
name|UUID_RANDOM
operator|.
name|nextBytes
argument_list|(
name|buf
argument_list|)
expr_stmt|;
return|return
name|UUID_ENCODING
operator|.
name|encode
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
literal|4
argument_list|)
operator|+
literal|'_'
operator|+
name|UUID_ENCODING
operator|.
name|encode
argument_list|(
name|buf
argument_list|,
literal|4
argument_list|,
literal|4
argument_list|)
return|;
block|}
comment|/**    * Get the next patch set ID from a previously-read map of all refs.    *    * @param allRefs map of full ref name to ref, in the same format returned by {@link    *     org.eclipse.jgit.lib.RefDatabase#getRefs(String)} when passing {@code ""}.    * @param id previous patch set ID.    * @return next unused patch set ID for the same change, skipping any IDs whose corresponding ref    *     names appear in the {@code allRefs} map.    */
DECL|method|nextPatchSetIdFromAllRefsMap (Map<String, Ref> allRefs, PatchSet.Id id)
specifier|public
specifier|static
name|PatchSet
operator|.
name|Id
name|nextPatchSetIdFromAllRefsMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|allRefs
parameter_list|,
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
name|PatchSet
operator|.
name|Id
name|next
init|=
name|nextPatchSetId
argument_list|(
name|id
argument_list|)
decl_stmt|;
while|while
condition|(
name|allRefs
operator|.
name|containsKey
argument_list|(
name|next
operator|.
name|toRefName
argument_list|()
argument_list|)
condition|)
block|{
name|next
operator|=
name|nextPatchSetId
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
return|return
name|next
return|;
block|}
comment|/**    * Get the next patch set ID from a previously-read map of refs below the change prefix.    *    * @param changeRefs map of ref suffix to SHA-1, where the keys are ref names with the {@code    *     refs/changes/CD/ABCD/} prefix stripped. All refs should be under {@code id}'s change ref    *     prefix. The keys match the format returned by {@link    *     org.eclipse.jgit.lib.RefDatabase#getRefs(String)} when passing the appropriate {@code    *     refs/changes/CD/ABCD}.    * @param id previous patch set ID.    * @return next unused patch set ID for the same change, skipping any IDs whose corresponding ref    *     names appear in the {@code changeRefs} map.    */
DECL|method|nextPatchSetIdFromChangeRefsMap ( Map<String, ObjectId> changeRefs, PatchSet.Id id)
specifier|public
specifier|static
name|PatchSet
operator|.
name|Id
name|nextPatchSetIdFromChangeRefsMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ObjectId
argument_list|>
name|changeRefs
parameter_list|,
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
name|int
name|prefixLen
init|=
name|id
operator|.
name|getParentKey
argument_list|()
operator|.
name|toRefPrefix
argument_list|()
operator|.
name|length
argument_list|()
decl_stmt|;
name|PatchSet
operator|.
name|Id
name|next
init|=
name|nextPatchSetId
argument_list|(
name|id
argument_list|)
decl_stmt|;
while|while
condition|(
name|changeRefs
operator|.
name|containsKey
argument_list|(
name|next
operator|.
name|toRefName
argument_list|()
operator|.
name|substring
argument_list|(
name|prefixLen
argument_list|)
argument_list|)
condition|)
block|{
name|next
operator|=
name|nextPatchSetId
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
return|return
name|next
return|;
block|}
comment|/**    * Get the next patch set ID just looking at a single previous patch set ID.    *    *<p>This patch set ID may or may not be available in the database; callers that want a    * previously-unused ID should use {@link #nextPatchSetIdFromAllRefsMap} or {@link    * #nextPatchSetIdFromChangeRefsMap}.    *    * @param id previous patch set ID.    * @return next patch set ID for the same change, incrementing by 1.    */
DECL|method|nextPatchSetId (PatchSet.Id id)
specifier|public
specifier|static
name|PatchSet
operator|.
name|Id
name|nextPatchSetId
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|id
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|id
operator|.
name|get
argument_list|()
operator|+
literal|1
argument_list|)
return|;
block|}
comment|/**    * Get the next patch set ID from scanning refs in the repo.    *    * @param git repository to scan for patch set refs.    * @param id previous patch set ID.    * @return next unused patch set ID for the same change, skipping any IDs whose corresponding ref    *     names appear in the repository.    */
DECL|method|nextPatchSetId (Repository git, PatchSet.Id id)
specifier|public
specifier|static
name|PatchSet
operator|.
name|Id
name|nextPatchSetId
parameter_list|(
name|Repository
name|git
parameter_list|,
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|nextPatchSetIdFromChangeRefsMap
argument_list|(
name|Maps
operator|.
name|transformValues
argument_list|(
name|git
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|id
operator|.
name|getParentKey
argument_list|()
operator|.
name|toRefPrefix
argument_list|()
argument_list|)
argument_list|,
name|Ref
operator|::
name|getObjectId
argument_list|)
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|cropSubject (String subject)
specifier|public
specifier|static
name|String
name|cropSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
if|if
condition|(
name|subject
operator|.
name|length
argument_list|()
operator|>
name|SUBJECT_MAX_LENGTH
condition|)
block|{
name|int
name|maxLength
init|=
name|SUBJECT_MAX_LENGTH
operator|-
name|SUBJECT_CROP_APPENDIX
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|cropPosition
init|=
name|maxLength
init|;
name|cropPosition
operator|>
name|maxLength
operator|-
name|SUBJECT_CROP_RANGE
condition|;
name|cropPosition
operator|--
control|)
block|{
if|if
condition|(
name|Character
operator|.
name|isWhitespace
argument_list|(
name|subject
operator|.
name|charAt
argument_list|(
name|cropPosition
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|subject
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|cropPosition
argument_list|)
operator|+
name|SUBJECT_CROP_APPENDIX
return|;
block|}
block|}
return|return
name|subject
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|maxLength
argument_list|)
operator|+
name|SUBJECT_CROP_APPENDIX
return|;
block|}
return|return
name|subject
return|;
block|}
DECL|method|status (Change c)
specifier|public
specifier|static
name|String
name|status
parameter_list|(
name|Change
name|c
parameter_list|)
block|{
return|return
name|c
operator|!=
literal|null
condition|?
name|c
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
else|:
literal|"deleted"
return|;
block|}
DECL|method|ChangeUtil ()
specifier|private
name|ChangeUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

