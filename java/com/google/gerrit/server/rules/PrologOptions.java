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
DECL|package|com.google.gerrit.server.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|rules
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
name|AutoValue
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
name|common
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|PrologOptions
specifier|public
specifier|abstract
class|class
name|PrologOptions
block|{
DECL|method|defaultOptions ()
specifier|public
specifier|static
name|PrologOptions
name|defaultOptions
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_PrologOptions
operator|.
name|Builder
argument_list|()
operator|.
name|logErrors
argument_list|(
literal|true
argument_list|)
operator|.
name|skipFilters
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|dryRunOptions (String ruleToTest, boolean skipFilters)
specifier|public
specifier|static
name|PrologOptions
name|dryRunOptions
parameter_list|(
name|String
name|ruleToTest
parameter_list|,
name|boolean
name|skipFilters
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_PrologOptions
operator|.
name|Builder
argument_list|()
operator|.
name|logErrors
argument_list|(
literal|false
argument_list|)
operator|.
name|skipFilters
argument_list|(
name|skipFilters
argument_list|)
operator|.
name|rule
argument_list|(
name|ruleToTest
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/** Whether errors should be logged. */
DECL|method|logErrors ()
specifier|abstract
name|boolean
name|logErrors
parameter_list|()
function_decl|;
comment|/** Whether Prolog filters from parent projects should be skipped. */
DECL|method|skipFilters ()
specifier|abstract
name|boolean
name|skipFilters
parameter_list|()
function_decl|;
comment|/**    * Prolog rule that should be run. If not given, the Prolog rule that is configured for the    * project is used (the rule from rules.pl in refs/meta/config).    */
DECL|method|rule ()
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|rule
parameter_list|()
function_decl|;
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|logErrors (boolean logErrors)
specifier|abstract
name|PrologOptions
operator|.
name|Builder
name|logErrors
parameter_list|(
name|boolean
name|logErrors
parameter_list|)
function_decl|;
DECL|method|skipFilters (boolean skipFilters)
specifier|abstract
name|PrologOptions
operator|.
name|Builder
name|skipFilters
parameter_list|(
name|boolean
name|skipFilters
parameter_list|)
function_decl|;
DECL|method|rule (@ullable String rule)
specifier|abstract
name|PrologOptions
operator|.
name|Builder
name|rule
parameter_list|(
annotation|@
name|Nullable
name|String
name|rule
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|abstract
name|PrologOptions
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

