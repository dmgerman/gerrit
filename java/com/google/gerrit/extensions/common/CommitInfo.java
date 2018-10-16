begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

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
name|joining
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
name|MoreObjects
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
name|MoreObjects
operator|.
name|ToStringHelper
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
name|Objects
import|;
end_import

begin_class
DECL|class|CommitInfo
specifier|public
class|class
name|CommitInfo
block|{
DECL|field|commit
specifier|public
name|String
name|commit
decl_stmt|;
DECL|field|parents
specifier|public
name|List
argument_list|<
name|CommitInfo
argument_list|>
name|parents
decl_stmt|;
DECL|field|author
specifier|public
name|GitPerson
name|author
decl_stmt|;
DECL|field|committer
specifier|public
name|GitPerson
name|committer
decl_stmt|;
DECL|field|subject
specifier|public
name|String
name|subject
decl_stmt|;
DECL|field|message
specifier|public
name|String
name|message
decl_stmt|;
DECL|field|webLinks
specifier|public
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
decl_stmt|;
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|CommitInfo
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|CommitInfo
name|c
init|=
operator|(
name|CommitInfo
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|commit
argument_list|,
name|c
operator|.
name|commit
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|parents
argument_list|,
name|c
operator|.
name|parents
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|author
argument_list|,
name|c
operator|.
name|author
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|committer
argument_list|,
name|c
operator|.
name|committer
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|subject
argument_list|,
name|c
operator|.
name|subject
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|message
argument_list|,
name|c
operator|.
name|message
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|webLinks
argument_list|,
name|c
operator|.
name|webLinks
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
name|commit
argument_list|,
name|parents
argument_list|,
name|author
argument_list|,
name|committer
argument_list|,
name|subject
argument_list|,
name|message
argument_list|,
name|webLinks
argument_list|)
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
name|ToStringHelper
name|helper
init|=
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|addValue
argument_list|(
name|commit
argument_list|)
decl_stmt|;
if|if
condition|(
name|parents
operator|!=
literal|null
condition|)
block|{
name|helper
operator|.
name|add
argument_list|(
literal|"parents"
argument_list|,
name|parents
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|p
lambda|->
name|p
operator|.
name|commit
argument_list|)
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|", "
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|helper
operator|.
name|add
argument_list|(
literal|"author"
argument_list|,
name|author
argument_list|)
operator|.
name|add
argument_list|(
literal|"committer"
argument_list|,
name|committer
argument_list|)
operator|.
name|add
argument_list|(
literal|"subject"
argument_list|,
name|subject
argument_list|)
operator|.
name|add
argument_list|(
literal|"message"
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|webLinks
operator|!=
literal|null
condition|)
block|{
name|helper
operator|.
name|add
argument_list|(
literal|"webLinks"
argument_list|,
name|webLinks
argument_list|)
expr_stmt|;
block|}
return|return
name|helper
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

