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
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|SubmitRecord
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
import|;
end_import

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
comment|/**  * Allows plugins to decide whether a change is ready to be submitted or not.  *  *<p>For a given {@link ChangeData}, each plugin is called and returns a {@link Collection} of  * {@link SubmitRecord}. This collection can be empty, or contain one or several values.  *  *<p>A Change can only be submitted if all the plugins give their consent.  *  *<p>Each {@link SubmitRecord} represents a decision made by the plugin. If the plugin rejects a  * change, it should hold valuable informations to help the end user understand and correct the  * blocking points.  *  *<p>It should be noted that each plugin can handle rules inheritance.  *  *<p>This interface should be used to write pre-submit validation rules. This includes both simple  * checks, coded in Java, and more complex fully fledged expression evaluators (think: Prolog,  * JavaCC, or even JavaScript rules).  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|SubmitRule
specifier|public
interface|interface
name|SubmitRule
block|{
comment|/** Returns a {@link Collection} of {@link SubmitRecord} status for the change. */
DECL|method|evaluate (ChangeData changeData)
name|Collection
argument_list|<
name|SubmitRecord
argument_list|>
name|evaluate
parameter_list|(
name|ChangeData
name|changeData
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

