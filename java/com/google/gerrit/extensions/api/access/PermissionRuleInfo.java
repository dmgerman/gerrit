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
DECL|package|com.google.gerrit.extensions.api.access
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|access
package|;
end_package

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
DECL|class|PermissionRuleInfo
specifier|public
class|class
name|PermissionRuleInfo
block|{
DECL|enum|Action
specifier|public
enum|enum
name|Action
block|{
DECL|enumConstant|ALLOW
name|ALLOW
block|,
DECL|enumConstant|DENY
name|DENY
block|,
DECL|enumConstant|BLOCK
name|BLOCK
block|,
DECL|enumConstant|INTERACTIVE
name|INTERACTIVE
block|,
DECL|enumConstant|BATCH
name|BATCH
block|}
DECL|field|action
specifier|public
name|Action
name|action
decl_stmt|;
DECL|field|force
specifier|public
name|Boolean
name|force
decl_stmt|;
DECL|field|min
specifier|public
name|Integer
name|min
decl_stmt|;
DECL|field|max
specifier|public
name|Integer
name|max
decl_stmt|;
DECL|method|PermissionRuleInfo (Action action, Boolean force)
specifier|public
name|PermissionRuleInfo
parameter_list|(
name|Action
name|action
parameter_list|,
name|Boolean
name|force
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
name|this
operator|.
name|force
operator|=
name|force
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|PermissionRuleInfo
condition|)
block|{
name|PermissionRuleInfo
name|p
init|=
operator|(
name|PermissionRuleInfo
operator|)
name|obj
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|action
argument_list|,
name|p
operator|.
name|action
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|force
argument_list|,
name|p
operator|.
name|force
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|min
argument_list|,
name|p
operator|.
name|min
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|max
argument_list|,
name|p
operator|.
name|max
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
name|action
argument_list|,
name|force
argument_list|,
name|min
argument_list|,
name|max
argument_list|)
return|;
block|}
block|}
end_class

end_unit

