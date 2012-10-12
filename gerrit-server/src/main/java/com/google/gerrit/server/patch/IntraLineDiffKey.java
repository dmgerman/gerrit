begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|diff
operator|.
name|Edit
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
name|List
import|;
end_import

begin_class
DECL|class|IntraLineDiffKey
specifier|public
class|class
name|IntraLineDiffKey
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4L
decl_stmt|;
DECL|field|ignoreWhitespace
specifier|private
specifier|transient
name|boolean
name|ignoreWhitespace
decl_stmt|;
DECL|field|aId
specifier|private
specifier|transient
name|ObjectId
name|aId
decl_stmt|;
DECL|field|bId
specifier|private
specifier|transient
name|ObjectId
name|bId
decl_stmt|;
comment|// Transient data passed through on cache misses to the loader.
DECL|field|aText
specifier|private
specifier|transient
name|Text
name|aText
decl_stmt|;
DECL|field|bText
specifier|private
specifier|transient
name|Text
name|bText
decl_stmt|;
DECL|field|edits
specifier|private
specifier|transient
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
decl_stmt|;
DECL|field|projectKey
specifier|private
specifier|transient
name|Project
operator|.
name|NameKey
name|projectKey
decl_stmt|;
DECL|field|commit
specifier|private
specifier|transient
name|ObjectId
name|commit
decl_stmt|;
DECL|field|path
specifier|private
specifier|transient
name|String
name|path
decl_stmt|;
DECL|method|IntraLineDiffKey (ObjectId aId, Text aText, ObjectId bId, Text bText, List<Edit> edits, Project.NameKey projectKey, ObjectId commit, String path, boolean ignoreWhitespace)
specifier|public
name|IntraLineDiffKey
parameter_list|(
name|ObjectId
name|aId
parameter_list|,
name|Text
name|aText
parameter_list|,
name|ObjectId
name|bId
parameter_list|,
name|Text
name|bText
parameter_list|,
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectKey
parameter_list|,
name|ObjectId
name|commit
parameter_list|,
name|String
name|path
parameter_list|,
name|boolean
name|ignoreWhitespace
parameter_list|)
block|{
name|this
operator|.
name|aId
operator|=
name|aId
expr_stmt|;
name|this
operator|.
name|bId
operator|=
name|bId
expr_stmt|;
name|this
operator|.
name|aText
operator|=
name|aText
expr_stmt|;
name|this
operator|.
name|bText
operator|=
name|bText
expr_stmt|;
name|this
operator|.
name|edits
operator|=
name|edits
expr_stmt|;
name|this
operator|.
name|projectKey
operator|=
name|projectKey
expr_stmt|;
name|this
operator|.
name|commit
operator|=
name|commit
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|ignoreWhitespace
operator|=
name|ignoreWhitespace
expr_stmt|;
block|}
DECL|method|getTextA ()
name|Text
name|getTextA
parameter_list|()
block|{
return|return
name|aText
return|;
block|}
DECL|method|getTextB ()
name|Text
name|getTextB
parameter_list|()
block|{
return|return
name|bText
return|;
block|}
DECL|method|getEdits ()
name|List
argument_list|<
name|Edit
argument_list|>
name|getEdits
parameter_list|()
block|{
return|return
name|edits
return|;
block|}
DECL|method|getBlobA ()
specifier|public
name|ObjectId
name|getBlobA
parameter_list|()
block|{
return|return
name|aId
return|;
block|}
DECL|method|getBlobB ()
specifier|public
name|ObjectId
name|getBlobB
parameter_list|()
block|{
return|return
name|bId
return|;
block|}
DECL|method|isIgnoreWhitespace ()
specifier|public
name|boolean
name|isIgnoreWhitespace
parameter_list|()
block|{
return|return
name|ignoreWhitespace
return|;
block|}
DECL|method|getProject ()
name|Project
operator|.
name|NameKey
name|getProject
parameter_list|()
block|{
return|return
name|projectKey
return|;
block|}
DECL|method|getCommit ()
name|ObjectId
name|getCommit
parameter_list|()
block|{
return|return
name|commit
return|;
block|}
DECL|method|getPath ()
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
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
name|int
name|h
init|=
literal|0
decl_stmt|;
name|h
operator|=
name|h
operator|*
literal|31
operator|+
name|aId
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|h
operator|=
name|h
operator|*
literal|31
operator|+
name|bId
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|h
operator|=
name|h
operator|*
literal|31
operator|+
operator|(
name|ignoreWhitespace
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|h
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
name|IntraLineDiffKey
condition|)
block|{
specifier|final
name|IntraLineDiffKey
name|k
init|=
operator|(
name|IntraLineDiffKey
operator|)
name|o
decl_stmt|;
return|return
name|aId
operator|.
name|equals
argument_list|(
name|k
operator|.
name|aId
argument_list|)
comment|//
operator|&&
name|bId
operator|.
name|equals
argument_list|(
name|k
operator|.
name|bId
argument_list|)
comment|//
operator|&&
name|ignoreWhitespace
operator|==
name|k
operator|.
name|ignoreWhitespace
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
literal|"IntraLineDiffKey["
argument_list|)
expr_stmt|;
if|if
condition|(
name|projectKey
operator|!=
literal|null
condition|)
block|{
name|n
operator|.
name|append
argument_list|(
name|projectKey
operator|.
name|get
argument_list|()
argument_list|)
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
name|aId
operator|.
name|name
argument_list|()
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
name|bId
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
name|writeNotNull
argument_list|(
name|out
argument_list|,
name|aId
argument_list|)
expr_stmt|;
name|writeNotNull
argument_list|(
name|out
argument_list|,
name|bId
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeBoolean
argument_list|(
name|ignoreWhitespace
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
name|aId
operator|=
name|readNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|bId
operator|=
name|readNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|ignoreWhitespace
operator|=
name|in
operator|.
name|readBoolean
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

