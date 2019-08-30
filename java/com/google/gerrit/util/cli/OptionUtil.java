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
DECL|package|com.google.gerrit.util.cli
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|util
operator|.
name|cli
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoAnnotation
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
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|OptionHandler
import|;
end_import

begin_comment
comment|/** Utilities to support creating new {@link Option} instances. */
end_comment

begin_class
DECL|class|OptionUtil
specifier|public
class|class
name|OptionUtil
block|{
annotation|@
name|AutoAnnotation
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|newOption ( String name, ImmutableList<String> aliases, String usage, String metaVar, boolean required, boolean help, boolean hidden, Class<? extends OptionHandler> handler, ImmutableList<String> depends, ImmutableList<String> forbids)
specifier|public
specifier|static
name|Option
name|newOption
parameter_list|(
name|String
name|name
parameter_list|,
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|aliases
parameter_list|,
name|String
name|usage
parameter_list|,
name|String
name|metaVar
parameter_list|,
name|boolean
name|required
parameter_list|,
name|boolean
name|help
parameter_list|,
name|boolean
name|hidden
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|OptionHandler
argument_list|>
name|handler
parameter_list|,
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|depends
parameter_list|,
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|forbids
parameter_list|)
block|{
return|return
operator|new
name|AutoAnnotation_OptionUtil_newOption
argument_list|(
name|name
argument_list|,
name|aliases
argument_list|,
name|usage
argument_list|,
name|metaVar
argument_list|,
name|required
argument_list|,
name|help
argument_list|,
name|hidden
argument_list|,
name|handler
argument_list|,
name|depends
argument_list|,
name|forbids
argument_list|)
return|;
block|}
block|}
end_class

end_unit

