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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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

begin_comment
comment|/**  * Stable identifier for options passed to a particular submit rule evaluator.  *  *<p>Used to test whether it is ok to reuse a cached list of submit records. Does not include a  * change or patch set ID; callers are responsible for checking those on their own.  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|SubmitRuleOptions
specifier|public
specifier|abstract
class|class
name|SubmitRuleOptions
block|{
DECL|field|defaults
specifier|private
specifier|static
specifier|final
name|SubmitRuleOptions
name|defaults
init|=
operator|new
name|AutoValue_SubmitRuleOptions
operator|.
name|Builder
argument_list|()
operator|.
name|allowClosed
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
DECL|method|defaults ()
specifier|public
specifier|static
name|SubmitRuleOptions
name|defaults
parameter_list|()
block|{
return|return
name|defaults
return|;
block|}
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
name|defaults
operator|.
name|toBuilder
argument_list|()
return|;
block|}
DECL|method|allowClosed ()
specifier|public
specifier|abstract
name|boolean
name|allowClosed
parameter_list|()
function_decl|;
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|Builder
name|toBuilder
parameter_list|()
function_decl|;
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|allowClosed (boolean allowClosed)
specifier|public
specifier|abstract
name|SubmitRuleOptions
operator|.
name|Builder
name|allowClosed
parameter_list|(
name|boolean
name|allowClosed
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|SubmitRuleOptions
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

