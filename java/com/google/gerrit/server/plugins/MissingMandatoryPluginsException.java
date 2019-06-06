begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/** Raised when one or more mandatory plugins are missing. */
end_comment

begin_class
DECL|class|MissingMandatoryPluginsException
specifier|public
class|class
name|MissingMandatoryPluginsException
extends|extends
name|RuntimeException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|MissingMandatoryPluginsException (Collection<String> pluginNames)
specifier|public
name|MissingMandatoryPluginsException
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|pluginNames
parameter_list|)
block|{
name|super
argument_list|(
name|getMessage
argument_list|(
name|pluginNames
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getMessage (Collection<String> pluginNames)
specifier|private
specifier|static
name|String
name|getMessage
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|pluginNames
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Cannot find or load the following mandatory plugins: %s"
argument_list|,
name|pluginNames
argument_list|)
return|;
block|}
block|}
end_class

end_unit

