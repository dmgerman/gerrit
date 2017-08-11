begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.conditions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|conditions
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_comment
comment|/**<b>DO NOT USE</b> */
end_comment

begin_class
DECL|class|PrivateInternals_BooleanCondition
specifier|public
specifier|final
class|class
name|PrivateInternals_BooleanCondition
block|{
DECL|method|PrivateInternals_BooleanCondition ()
specifier|private
name|PrivateInternals_BooleanCondition
parameter_list|()
block|{}
DECL|class|SubclassOnlyInCoreServer
specifier|public
specifier|abstract
specifier|static
class|class
name|SubclassOnlyInCoreServer
extends|extends
name|BooleanCondition
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|children (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|T
argument_list|>
name|children
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|singleton
argument_list|(
operator|(
name|T
operator|)
name|this
argument_list|)
return|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

