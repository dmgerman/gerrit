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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toSet
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
name|entities
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
name|entities
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
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|number
argument_list|)
argument_list|)
decl_stmt|;
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
comment|/**    * Get the next patch set ID from a previously-read map of refs below the change prefix.    *    * @param changeRefNames existing full change ref names with the same change ID as {@code id}.    * @param id previous patch set ID.    * @return next unused patch set ID for the same change, skipping any IDs whose corresponding ref    *     names appear in the {@code changeRefs} map.    */
DECL|method|nextPatchSetIdFromChangeRefs ( Collection<String> changeRefNames, PatchSet.Id id)
specifier|public
specifier|static
name|PatchSet
operator|.
name|Id
name|nextPatchSetIdFromChangeRefs
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|changeRefNames
parameter_list|,
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|nextPatchSetIdFromChangeRefs
argument_list|(
name|changeRefNames
operator|.
name|stream
argument_list|()
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|nextPatchSetIdFromChangeRefs ( Stream<String> changeRefNames, PatchSet.Id id)
specifier|private
specifier|static
name|PatchSet
operator|.
name|Id
name|nextPatchSetIdFromChangeRefs
parameter_list|(
name|Stream
argument_list|<
name|String
argument_list|>
name|changeRefNames
parameter_list|,
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
name|Set
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|existing
init|=
name|changeRefNames
operator|.
name|map
argument_list|(
name|PatchSet
operator|.
name|Id
operator|::
name|fromRef
argument_list|)
operator|.
name|filter
argument_list|(
name|psId
lambda|->
name|psId
operator|!=
literal|null
operator|&&
name|psId
operator|.
name|changeId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
operator|.
name|changeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
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
name|existing
operator|.
name|contains
argument_list|(
name|next
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
comment|/**    * Get the next patch set ID just looking at a single previous patch set ID.    *    *<p>This patch set ID may or may not be available in the database.    *    * @param id previous patch set ID.    * @return next patch set ID for the same change, incrementing by 1.    */
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
name|PatchSet
operator|.
name|id
argument_list|(
name|id
operator|.
name|changeId
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
name|nextPatchSetIdFromChangeRefs
argument_list|(
name|git
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefsByPrefix
argument_list|(
name|id
operator|.
name|changeId
argument_list|()
operator|.
name|toRefPrefix
argument_list|()
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Ref
operator|::
name|getName
argument_list|)
argument_list|,
name|id
argument_list|)
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

