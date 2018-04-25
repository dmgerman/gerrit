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
name|Objects
import|;
end_import

begin_class
DECL|class|ChangeMessageInfo
specifier|public
class|class
name|ChangeMessageInfo
block|{
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|tag
specifier|public
name|String
name|tag
decl_stmt|;
DECL|field|author
specifier|public
name|AccountInfo
name|author
decl_stmt|;
DECL|field|realAuthor
specifier|public
name|AccountInfo
name|realAuthor
decl_stmt|;
DECL|field|date
specifier|public
name|Timestamp
name|date
decl_stmt|;
DECL|field|message
specifier|public
name|String
name|message
decl_stmt|;
DECL|field|_revisionNumber
specifier|public
name|Integer
name|_revisionNumber
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
name|o
operator|instanceof
name|ChangeMessageInfo
condition|)
block|{
name|ChangeMessageInfo
name|cmi
init|=
operator|(
name|ChangeMessageInfo
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|id
argument_list|,
name|cmi
operator|.
name|id
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|tag
argument_list|,
name|cmi
operator|.
name|tag
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|author
argument_list|,
name|cmi
operator|.
name|author
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|realAuthor
argument_list|,
name|cmi
operator|.
name|realAuthor
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|date
argument_list|,
name|cmi
operator|.
name|date
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|message
argument_list|,
name|cmi
operator|.
name|message
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|_revisionNumber
argument_list|,
name|cmi
operator|.
name|_revisionNumber
argument_list|)
return|;
block|}
return|return
literal|false
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
name|id
argument_list|,
name|tag
argument_list|,
name|author
argument_list|,
name|realAuthor
argument_list|,
name|date
argument_list|,
name|message
argument_list|,
name|_revisionNumber
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
return|return
literal|"ChangeMessageInfo{"
operator|+
literal|"id="
operator|+
name|id
operator|+
literal|", tag="
operator|+
name|tag
operator|+
literal|", author="
operator|+
name|author
operator|+
literal|", realAuthor="
operator|+
name|realAuthor
operator|+
literal|", date="
operator|+
name|date
operator|+
literal|", _revisionNumber"
operator|+
name|_revisionNumber
operator|+
literal|", message=["
operator|+
name|message
operator|+
literal|"]}"
return|;
block|}
block|}
end_class

end_unit

