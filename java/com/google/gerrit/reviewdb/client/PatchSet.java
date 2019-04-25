begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|base
operator|.
name|Splitter
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
name|ImmutableList
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
name|Streams
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
name|primitives
operator|.
name|Ints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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

begin_comment
comment|/** A single revision of a {@link Change}. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|PatchSet
specifier|public
specifier|abstract
class|class
name|PatchSet
block|{
comment|/** Is the reference name a change reference? */
DECL|method|isChangeRef (String name)
specifier|public
specifier|static
name|boolean
name|isChangeRef
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Id
operator|.
name|fromRef
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|/**    * Is the reference name a change reference?    *    * @deprecated use isChangeRef instead.    */
annotation|@
name|Deprecated
DECL|method|isRef (String name)
specifier|public
specifier|static
name|boolean
name|isRef
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|isChangeRef
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|joinGroups (List<String> groups)
specifier|public
specifier|static
name|String
name|joinGroups
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|groups
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|group
range|:
name|groups
control|)
block|{
name|checkArgument
argument_list|(
operator|!
name|group
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
argument_list|,
literal|"group may not contain ',': %s"
argument_list|,
name|group
argument_list|)
expr_stmt|;
block|}
return|return
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|groups
argument_list|)
return|;
block|}
DECL|method|splitGroups (String joinedGroups)
specifier|public
specifier|static
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|splitGroups
parameter_list|(
name|String
name|joinedGroups
parameter_list|)
block|{
return|return
name|Streams
operator|.
name|stream
argument_list|(
name|Splitter
operator|.
name|on
argument_list|(
literal|','
argument_list|)
operator|.
name|split
argument_list|(
name|joinedGroups
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|id (Change.Id changeId, int id)
specifier|public
specifier|static
name|Id
name|id
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|int
name|id
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_PatchSet_Id
argument_list|(
name|changeId
argument_list|,
name|id
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
DECL|class|Id
specifier|public
specifier|abstract
specifier|static
class|class
name|Id
block|{
comment|/** Parse a PatchSet.Id out of a string representation. */
DECL|method|parse (String str)
specifier|public
specifier|static
name|Id
name|parse
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|','
argument_list|)
operator|.
name|splitToList
argument_list|(
name|str
argument_list|)
decl_stmt|;
name|checkIdFormat
argument_list|(
name|parts
operator|.
name|size
argument_list|()
operator|==
literal|2
argument_list|,
name|str
argument_list|)
expr_stmt|;
name|Integer
name|changeId
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|checkIdFormat
argument_list|(
name|changeId
operator|!=
literal|null
argument_list|,
name|str
argument_list|)
expr_stmt|;
name|Integer
name|id
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|checkIdFormat
argument_list|(
name|id
operator|!=
literal|null
argument_list|,
name|str
argument_list|)
expr_stmt|;
return|return
name|PatchSet
operator|.
name|id
argument_list|(
name|Change
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|checkIdFormat (boolean test, String input)
specifier|private
specifier|static
name|void
name|checkIdFormat
parameter_list|(
name|boolean
name|test
parameter_list|,
name|String
name|input
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|test
argument_list|,
literal|"invalid patch set ID: %s"
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
comment|/** Parse a PatchSet.Id from a {@link #refName()} result. */
DECL|method|fromRef (String ref)
specifier|public
specifier|static
name|Id
name|fromRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|int
name|cs
init|=
name|Change
operator|.
name|Id
operator|.
name|startIndex
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|cs
operator|<
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|ce
init|=
name|Change
operator|.
name|Id
operator|.
name|nextNonDigit
argument_list|(
name|ref
argument_list|,
name|cs
argument_list|)
decl_stmt|;
name|int
name|patchSetId
init|=
name|fromRef
argument_list|(
name|ref
argument_list|,
name|ce
argument_list|)
decl_stmt|;
if|if
condition|(
name|patchSetId
operator|<
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|changeId
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|ref
operator|.
name|substring
argument_list|(
name|cs
argument_list|,
name|ce
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|PatchSet
operator|.
name|id
argument_list|(
name|Change
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
argument_list|,
name|patchSetId
argument_list|)
return|;
block|}
DECL|method|fromRef (String ref, int changeIdEnd)
specifier|static
name|int
name|fromRef
parameter_list|(
name|String
name|ref
parameter_list|,
name|int
name|changeIdEnd
parameter_list|)
block|{
comment|// Patch set ID.
name|int
name|ps
init|=
name|changeIdEnd
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|ps
operator|>=
name|ref
operator|.
name|length
argument_list|()
operator|||
name|ref
operator|.
name|charAt
argument_list|(
name|ps
argument_list|)
operator|==
literal|'0'
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
for|for
control|(
name|int
name|i
init|=
name|ps
init|;
name|i
operator|<
name|ref
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|ref
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|<
literal|'0'
operator|||
name|ref
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|>
literal|'9'
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|ref
operator|.
name|substring
argument_list|(
name|ps
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toId (int number)
specifier|public
specifier|static
name|String
name|toId
parameter_list|(
name|int
name|number
parameter_list|)
block|{
return|return
name|number
operator|==
literal|0
condition|?
literal|"edit"
else|:
name|String
operator|.
name|valueOf
argument_list|(
name|number
argument_list|)
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|toId
argument_list|(
name|id
argument_list|()
argument_list|)
return|;
block|}
DECL|method|changeId ()
specifier|public
specifier|abstract
name|Change
operator|.
name|Id
name|changeId
parameter_list|()
function_decl|;
DECL|method|id ()
specifier|abstract
name|int
name|id
parameter_list|()
function_decl|;
DECL|method|get ()
specifier|public
name|int
name|get
parameter_list|()
block|{
return|return
name|id
argument_list|()
return|;
block|}
DECL|method|toRefName ()
specifier|public
name|String
name|toRefName
parameter_list|()
block|{
return|return
name|changeId
argument_list|()
operator|.
name|refPrefixBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|id
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|changeId
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|','
operator|+
name|id
argument_list|()
return|;
block|}
block|}
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_PatchSet
operator|.
name|Builder
argument_list|()
operator|.
name|groups
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|id (Id id)
specifier|public
specifier|abstract
name|Builder
name|id
parameter_list|(
name|Id
name|id
parameter_list|)
function_decl|;
DECL|method|id ()
specifier|public
specifier|abstract
name|Id
name|id
parameter_list|()
function_decl|;
DECL|method|commitId (ObjectId commitId)
specifier|public
specifier|abstract
name|Builder
name|commitId
parameter_list|(
name|ObjectId
name|commitId
parameter_list|)
function_decl|;
DECL|method|commitId ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|commitId
parameter_list|()
function_decl|;
DECL|method|uploader (Account.Id uploader)
specifier|public
specifier|abstract
name|Builder
name|uploader
parameter_list|(
name|Account
operator|.
name|Id
name|uploader
parameter_list|)
function_decl|;
DECL|method|createdOn (Timestamp createdOn)
specifier|public
specifier|abstract
name|Builder
name|createdOn
parameter_list|(
name|Timestamp
name|createdOn
parameter_list|)
function_decl|;
DECL|method|groups (Iterable<String> groups)
specifier|public
specifier|abstract
name|Builder
name|groups
parameter_list|(
name|Iterable
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
function_decl|;
DECL|method|groups ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|()
function_decl|;
DECL|method|pushCertificate (Optional<String> pushCertificate)
specifier|public
specifier|abstract
name|Builder
name|pushCertificate
parameter_list|(
name|Optional
argument_list|<
name|String
argument_list|>
name|pushCertificate
parameter_list|)
function_decl|;
DECL|method|pushCertificate (String pushCertificate)
specifier|public
specifier|abstract
name|Builder
name|pushCertificate
parameter_list|(
name|String
name|pushCertificate
parameter_list|)
function_decl|;
DECL|method|description (Optional<String> description)
specifier|public
specifier|abstract
name|Builder
name|description
parameter_list|(
name|Optional
argument_list|<
name|String
argument_list|>
name|description
parameter_list|)
function_decl|;
DECL|method|description (String description)
specifier|public
specifier|abstract
name|Builder
name|description
parameter_list|(
name|String
name|description
parameter_list|)
function_decl|;
DECL|method|description ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|description
parameter_list|()
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|PatchSet
name|build
parameter_list|()
function_decl|;
block|}
comment|/** ID of the patch set. */
DECL|method|id ()
specifier|public
specifier|abstract
name|Id
name|id
parameter_list|()
function_decl|;
comment|/**    * Commit ID of the patch set, also known as the revision.    *    *<p>If this is a deserialized instance that was originally serialized by an older version of    * Gerrit, and the old data erroneously did not include a {@code commitId}, then this method will    * return {@link ObjectId#zeroId()}.    */
DECL|method|commitId ()
specifier|public
specifier|abstract
name|ObjectId
name|commitId
parameter_list|()
function_decl|;
comment|/**    * Account that uploaded the patch set.    *    *<p>If this is a deserialized instance that was originally serialized by an older version of    * Gerrit, and the old data erroneously did not include an {@code uploader}, then this method will    * return an account ID of 0.    */
DECL|method|uploader ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|uploader
parameter_list|()
function_decl|;
comment|/**    * When this patch set was first introduced onto the change.    *    *<p>If this is a deserialized instance that was originally serialized by an older version of    * Gerrit, and the old data erroneously did not include a {@code createdOn}, then this method will    * return a timestamp of 0.    */
DECL|method|createdOn ()
specifier|public
specifier|abstract
name|Timestamp
name|createdOn
parameter_list|()
function_decl|;
comment|/**    * Opaque group identifier, usually assigned during creation.    *    *<p>This field is actually a comma-separated list of values, as in rare cases involving merge    * commits a patch set may belong to multiple groups.    *    *<p>Changes on the same branch having patch sets with intersecting groups are considered    * related, as in the "Related Changes" tab.    */
DECL|method|groups ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|()
function_decl|;
comment|/** Certificate sent with a push that created this patch set. */
DECL|method|pushCertificate ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|pushCertificate
parameter_list|()
function_decl|;
comment|/**    * Optional user-supplied description for this patch set.    *    *<p>When this field is an empty {@code Optional}, the description was never set on the patch    * set. When this field is present but an empty string, the description was set and later cleared.    */
DECL|method|description ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|description
parameter_list|()
function_decl|;
comment|/** Patch set number. */
DECL|method|number ()
specifier|public
name|int
name|number
parameter_list|()
block|{
return|return
name|id
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
comment|/** Name of the corresponding patch set ref. */
DECL|method|refName ()
specifier|public
name|String
name|refName
parameter_list|()
block|{
return|return
name|id
argument_list|()
operator|.
name|toRefName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

