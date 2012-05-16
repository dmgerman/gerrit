begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.registration
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Binding
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
name|Inject
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
name|Injector
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
name|Provider
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
name|TypeLiteral
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
DECL|class|DynamicMapProvider
class|class
name|DynamicMapProvider
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Provider
argument_list|<
name|DynamicMap
argument_list|<
name|T
argument_list|>
argument_list|>
block|{
DECL|field|type
specifier|private
specifier|final
name|TypeLiteral
argument_list|<
name|T
argument_list|>
name|type
decl_stmt|;
annotation|@
name|Inject
DECL|field|injector
specifier|private
name|Injector
name|injector
decl_stmt|;
DECL|method|DynamicMapProvider (TypeLiteral<T> type)
name|DynamicMapProvider
parameter_list|(
name|TypeLiteral
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|DynamicMap
argument_list|<
name|T
argument_list|>
name|get
parameter_list|()
block|{
name|PrivateInternals_DynamicMapImpl
argument_list|<
name|T
argument_list|>
name|m
init|=
operator|new
name|PrivateInternals_DynamicMapImpl
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Binding
argument_list|<
name|T
argument_list|>
argument_list|>
name|bindings
init|=
name|injector
operator|.
name|findBindingsByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|bindings
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Binding
argument_list|<
name|T
argument_list|>
name|b
range|:
name|bindings
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getKey
argument_list|()
operator|.
name|getAnnotation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
literal|"gerrit"
argument_list|,
name|b
operator|.
name|getKey
argument_list|()
argument_list|,
name|b
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|m
return|;
block|}
block|}
end_class

end_unit

