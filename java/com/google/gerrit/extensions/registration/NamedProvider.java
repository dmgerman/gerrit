begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
name|Provider
import|;
end_import

begin_comment
comment|/** Pair of provider implementation and plugin providing it. */
end_comment

begin_class
DECL|class|NamedProvider
class|class
name|NamedProvider
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|impl
specifier|final
name|Provider
argument_list|<
name|T
argument_list|>
name|impl
decl_stmt|;
DECL|field|pluginName
specifier|final
name|String
name|pluginName
decl_stmt|;
DECL|method|NamedProvider (Provider<T> provider, String pluginName)
name|NamedProvider
parameter_list|(
name|Provider
argument_list|<
name|T
argument_list|>
name|provider
parameter_list|,
name|String
name|pluginName
parameter_list|)
block|{
name|this
operator|.
name|impl
operator|=
name|provider
expr_stmt|;
name|this
operator|.
name|pluginName
operator|=
name|pluginName
expr_stmt|;
block|}
block|}
end_class

end_unit

