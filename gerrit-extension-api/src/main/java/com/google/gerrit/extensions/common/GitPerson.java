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
DECL|class|GitPerson
specifier|public
class|class
name|GitPerson
block|{
DECL|field|name
specifier|public
name|String
name|name
decl_stmt|;
DECL|field|email
specifier|public
name|String
name|email
decl_stmt|;
DECL|field|date
specifier|public
name|Timestamp
name|date
decl_stmt|;
DECL|field|tz
specifier|public
name|int
name|tz
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
name|GitPerson
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|GitPerson
name|p
init|=
operator|(
name|GitPerson
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|name
argument_list|,
name|p
operator|.
name|name
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|email
argument_list|,
name|p
operator|.
name|email
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|date
argument_list|,
name|p
operator|.
name|date
argument_list|)
operator|&&
name|tz
operator|==
name|p
operator|.
name|tz
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
name|name
argument_list|,
name|email
argument_list|,
name|date
argument_list|,
name|tz
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
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"{name="
operator|+
name|name
operator|+
literal|", email="
operator|+
name|email
operator|+
literal|", date="
operator|+
name|date
operator|+
literal|", tz="
operator|+
name|tz
operator|+
literal|"}"
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

