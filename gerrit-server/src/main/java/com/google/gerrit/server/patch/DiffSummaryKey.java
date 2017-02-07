begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|extensions
operator|.
name|client
operator|.
name|DiffPreferencesInfo
operator|.
name|Whitespace
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
name|Objects
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

begin_class
DECL|class|DiffSummaryKey
specifier|public
class|class
name|DiffSummaryKey
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|public
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
comment|/** see PatchListKey#oldId */
DECL|field|oldId
specifier|private
specifier|transient
name|ObjectId
name|oldId
decl_stmt|;
comment|/** see PatchListKey#parentNum */
DECL|field|parentNum
specifier|private
specifier|transient
name|Integer
name|parentNum
decl_stmt|;
DECL|field|newId
specifier|private
specifier|transient
name|ObjectId
name|newId
decl_stmt|;
DECL|field|whitespace
specifier|private
specifier|transient
name|Whitespace
name|whitespace
decl_stmt|;
DECL|method|fromPatchListKey (PatchListKey plk)
specifier|public
specifier|static
name|DiffSummaryKey
name|fromPatchListKey
parameter_list|(
name|PatchListKey
name|plk
parameter_list|)
block|{
return|return
operator|new
name|DiffSummaryKey
argument_list|(
name|plk
operator|.
name|getOldId
argument_list|()
argument_list|,
name|plk
operator|.
name|getParentNum
argument_list|()
argument_list|,
name|plk
operator|.
name|getNewId
argument_list|()
argument_list|,
name|plk
operator|.
name|getWhitespace
argument_list|()
argument_list|)
return|;
block|}
DECL|method|DiffSummaryKey (ObjectId oldId, Integer parentNum, ObjectId newId, Whitespace whitespace)
specifier|private
name|DiffSummaryKey
parameter_list|(
name|ObjectId
name|oldId
parameter_list|,
name|Integer
name|parentNum
parameter_list|,
name|ObjectId
name|newId
parameter_list|,
name|Whitespace
name|whitespace
parameter_list|)
block|{
name|this
operator|.
name|oldId
operator|=
name|oldId
expr_stmt|;
name|this
operator|.
name|parentNum
operator|=
name|parentNum
expr_stmt|;
name|this
operator|.
name|newId
operator|=
name|newId
expr_stmt|;
name|this
operator|.
name|whitespace
operator|=
name|whitespace
expr_stmt|;
block|}
DECL|method|toPatchListKey ()
name|PatchListKey
name|toPatchListKey
parameter_list|()
block|{
return|return
operator|new
name|PatchListKey
argument_list|(
name|oldId
argument_list|,
name|parentNum
argument_list|,
name|newId
argument_list|,
name|whitespace
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|oldId
argument_list|,
name|parentNum
argument_list|,
name|newId
argument_list|,
name|whitespace
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (final Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
specifier|final
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|DiffSummaryKey
condition|)
block|{
name|DiffSummaryKey
name|k
init|=
operator|(
name|DiffSummaryKey
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|oldId
argument_list|,
name|k
operator|.
name|oldId
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|parentNum
argument_list|,
name|k
operator|.
name|parentNum
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|newId
argument_list|,
name|k
operator|.
name|newId
argument_list|)
operator|&&
name|whitespace
operator|==
name|k
operator|.
name|whitespace
return|;
block|}
return|return
literal|false
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
name|StringBuilder
name|n
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|n
operator|.
name|append
argument_list|(
literal|"DiffSummaryKey["
argument_list|)
expr_stmt|;
name|n
operator|.
name|append
argument_list|(
name|oldId
operator|!=
literal|null
condition|?
name|oldId
operator|.
name|name
argument_list|()
else|:
literal|"BASE"
argument_list|)
expr_stmt|;
name|n
operator|.
name|append
argument_list|(
literal|".."
argument_list|)
expr_stmt|;
name|n
operator|.
name|append
argument_list|(
name|newId
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
if|if
condition|(
name|parentNum
operator|!=
literal|null
condition|)
block|{
name|n
operator|.
name|append
argument_list|(
name|parentNum
argument_list|)
expr_stmt|;
name|n
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|n
operator|.
name|append
argument_list|(
name|whitespace
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|n
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|n
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|writeObject (final ObjectOutputStream out)
specifier|private
name|void
name|writeObject
parameter_list|(
specifier|final
name|ObjectOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|writeCanBeNull
argument_list|(
name|out
argument_list|,
name|oldId
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|parentNum
operator|==
literal|null
condition|?
literal|0
else|:
name|parentNum
argument_list|)
expr_stmt|;
name|writeNotNull
argument_list|(
name|out
argument_list|,
name|newId
argument_list|)
expr_stmt|;
name|Character
name|c
init|=
name|PatchListKey
operator|.
name|WHITESPACE_TYPES
operator|.
name|get
argument_list|(
name|whitespace
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid whitespace type: "
operator|+
name|whitespace
argument_list|)
throw|;
block|}
name|out
operator|.
name|writeChar
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
DECL|method|readObject (final ObjectInputStream in)
specifier|private
name|void
name|readObject
parameter_list|(
specifier|final
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|oldId
operator|=
name|readCanBeNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|int
name|n
init|=
name|in
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|parentNum
operator|=
name|n
operator|==
literal|0
condition|?
literal|null
else|:
name|Integer
operator|.
name|valueOf
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|newId
operator|=
name|readNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|char
name|t
init|=
name|in
operator|.
name|readChar
argument_list|()
decl_stmt|;
name|whitespace
operator|=
name|PatchListKey
operator|.
name|WHITESPACE_TYPES
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|t
argument_list|)
expr_stmt|;
if|if
condition|(
name|whitespace
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid whitespace type code: "
operator|+
name|t
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

