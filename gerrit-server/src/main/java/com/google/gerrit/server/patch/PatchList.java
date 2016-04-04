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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readBytes
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readVarInt32
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeBytes
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeVarInt32
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectIdSerialization
operator|.
name|readCanBeNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectIdSerialization
operator|.
name|readNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectIdSerialization
operator|.
name|writeCanBeNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectIdSerialization
operator|.
name|writeNotNull
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
name|common
operator|.
name|Nullable
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
name|Patch
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|AnyObjectId
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
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|zip
operator|.
name|DeflaterOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|InflaterInputStream
import|;
end_import

begin_class
DECL|class|PatchList
specifier|public
class|class
name|PatchList
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
name|PatchListKey
operator|.
name|serialVersionUID
decl_stmt|;
DECL|field|PATCH_CMP
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|PatchListEntry
argument_list|>
name|PATCH_CMP
init|=
operator|new
name|Comparator
argument_list|<
name|PatchListEntry
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|PatchListEntry
name|a
parameter_list|,
specifier|final
name|PatchListEntry
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getNewName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getNewName
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|Nullable
DECL|field|oldId
specifier|private
specifier|transient
name|ObjectId
name|oldId
decl_stmt|;
DECL|field|newId
specifier|private
specifier|transient
name|ObjectId
name|newId
decl_stmt|;
DECL|field|againstParent
specifier|private
specifier|transient
name|boolean
name|againstParent
decl_stmt|;
DECL|field|insertions
specifier|private
specifier|transient
name|int
name|insertions
decl_stmt|;
DECL|field|deletions
specifier|private
specifier|transient
name|int
name|deletions
decl_stmt|;
DECL|field|patches
specifier|private
specifier|transient
name|PatchListEntry
index|[]
name|patches
decl_stmt|;
DECL|method|PatchList (@ullable final AnyObjectId oldId, final AnyObjectId newId, final boolean againstParent, final PatchListEntry[] patches)
specifier|public
name|PatchList
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|AnyObjectId
name|oldId
parameter_list|,
specifier|final
name|AnyObjectId
name|newId
parameter_list|,
specifier|final
name|boolean
name|againstParent
parameter_list|,
specifier|final
name|PatchListEntry
index|[]
name|patches
parameter_list|)
block|{
name|this
operator|.
name|oldId
operator|=
name|oldId
operator|!=
literal|null
condition|?
name|oldId
operator|.
name|copy
argument_list|()
else|:
literal|null
expr_stmt|;
name|this
operator|.
name|newId
operator|=
name|newId
operator|.
name|copy
argument_list|()
expr_stmt|;
name|this
operator|.
name|againstParent
operator|=
name|againstParent
expr_stmt|;
comment|// We assume index 0 contains the magic commit message entry.
if|if
condition|(
name|patches
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|Arrays
operator|.
name|sort
argument_list|(
name|patches
argument_list|,
literal|1
argument_list|,
name|patches
operator|.
name|length
argument_list|,
name|PATCH_CMP
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|patches
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|insertions
operator|+=
name|patches
index|[
name|i
index|]
operator|.
name|getInsertions
argument_list|()
expr_stmt|;
name|deletions
operator|+=
name|patches
index|[
name|i
index|]
operator|.
name|getDeletions
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|patches
operator|=
name|patches
expr_stmt|;
block|}
comment|/** Old side tree or commit; null only if this is a combined diff. */
annotation|@
name|Nullable
DECL|method|getOldId ()
specifier|public
name|ObjectId
name|getOldId
parameter_list|()
block|{
return|return
name|oldId
return|;
block|}
comment|/** New side commit. */
DECL|method|getNewId ()
specifier|public
name|ObjectId
name|getNewId
parameter_list|()
block|{
return|return
name|newId
return|;
block|}
comment|/** Get a sorted, unmodifiable list of all files in this list. */
DECL|method|getPatches ()
specifier|public
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|getPatches
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|patches
argument_list|)
argument_list|)
return|;
block|}
comment|/** @return true if {@link #getOldId} is {@link #getNewId}'s ancestor. */
DECL|method|isAgainstParent ()
specifier|public
name|boolean
name|isAgainstParent
parameter_list|()
block|{
return|return
name|againstParent
return|;
block|}
comment|/** @return total number of new lines added. */
DECL|method|getInsertions ()
specifier|public
name|int
name|getInsertions
parameter_list|()
block|{
return|return
name|insertions
return|;
block|}
comment|/** @return total number of lines removed. */
DECL|method|getDeletions ()
specifier|public
name|int
name|getDeletions
parameter_list|()
block|{
return|return
name|deletions
return|;
block|}
comment|/**    * Get a sorted, modifiable list of all files in this list.    *<p>    * The returned list items do not populate:    *<ul>    *<li>{@link Patch#getCommentCount()}    *<li>{@link Patch#getDraftCount()}    *<li>{@link Patch#isReviewedByCurrentUser()}    *</ul>    *    * @param setId the patch set identity these patches belong to. This really    *        should not need to be specified, but is a current legacy artifact of    *        how the cache is keyed versus how the database is keyed.    */
DECL|method|toPatchList (final PatchSet.Id setId)
specifier|public
name|List
argument_list|<
name|Patch
argument_list|>
name|toPatchList
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|setId
parameter_list|)
block|{
specifier|final
name|ArrayList
argument_list|<
name|Patch
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|patches
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|PatchListEntry
name|e
range|:
name|patches
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|e
operator|.
name|toPatch
argument_list|(
name|setId
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
comment|/** Find an entry by name, returning an empty entry if not present. */
DECL|method|get (final String fileName)
specifier|public
name|PatchListEntry
name|get
parameter_list|(
specifier|final
name|String
name|fileName
parameter_list|)
block|{
specifier|final
name|int
name|index
init|=
name|search
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
return|return
literal|0
operator|<=
name|index
condition|?
name|patches
index|[
name|index
index|]
else|:
name|PatchListEntry
operator|.
name|empty
argument_list|(
name|fileName
argument_list|)
return|;
block|}
DECL|method|search (final String fileName)
specifier|private
name|int
name|search
parameter_list|(
specifier|final
name|String
name|fileName
parameter_list|)
block|{
if|if
condition|(
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|fileName
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|high
init|=
name|patches
operator|.
name|length
decl_stmt|;
name|int
name|low
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|low
operator|<
name|high
condition|)
block|{
specifier|final
name|int
name|mid
init|=
operator|(
name|low
operator|+
name|high
operator|)
operator|>>>
literal|1
decl_stmt|;
specifier|final
name|int
name|cmp
init|=
name|patches
index|[
name|mid
index|]
operator|.
name|getNewName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|<
literal|0
condition|)
block|{
name|low
operator|=
name|mid
operator|+
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cmp
operator|==
literal|0
condition|)
block|{
return|return
name|mid
return|;
block|}
else|else
block|{
name|high
operator|=
name|mid
expr_stmt|;
block|}
block|}
return|return
operator|-
operator|(
name|low
operator|+
literal|1
operator|)
return|;
block|}
DECL|method|writeObject (final ObjectOutputStream output)
specifier|private
name|void
name|writeObject
parameter_list|(
specifier|final
name|ObjectOutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|ByteArrayOutputStream
name|buf
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
init|(
name|DeflaterOutputStream
name|out
init|=
operator|new
name|DeflaterOutputStream
argument_list|(
name|buf
argument_list|)
init|)
block|{
name|writeCanBeNull
argument_list|(
name|out
argument_list|,
name|oldId
argument_list|)
expr_stmt|;
name|writeNotNull
argument_list|(
name|out
argument_list|,
name|newId
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|againstParent
condition|?
literal|1
else|:
literal|0
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|insertions
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|deletions
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|patches
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|PatchListEntry
name|p
range|:
name|patches
control|)
block|{
name|p
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
name|writeBytes
argument_list|(
name|output
argument_list|,
name|buf
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|readObject (final ObjectInputStream input)
specifier|private
name|void
name|readObject
parameter_list|(
specifier|final
name|ObjectInputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|ByteArrayInputStream
name|buf
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|readBytes
argument_list|(
name|input
argument_list|)
argument_list|)
decl_stmt|;
try|try
init|(
name|InflaterInputStream
name|in
init|=
operator|new
name|InflaterInputStream
argument_list|(
name|buf
argument_list|)
init|)
block|{
name|oldId
operator|=
name|readCanBeNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|newId
operator|=
name|readNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|againstParent
operator|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
operator|!=
literal|0
expr_stmt|;
name|insertions
operator|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|deletions
operator|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
expr_stmt|;
specifier|final
name|int
name|cnt
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|PatchListEntry
index|[]
name|all
init|=
operator|new
name|PatchListEntry
index|[
name|cnt
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|all
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|all
index|[
name|i
index|]
operator|=
name|PatchListEntry
operator|.
name|readFrom
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
name|patches
operator|=
name|all
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

