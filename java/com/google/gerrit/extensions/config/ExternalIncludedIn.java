begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|config
package|;
end_package

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
name|ListMultimap
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|ExternalIncludedIn
specifier|public
interface|interface
name|ExternalIncludedIn
block|{
comment|/**    * Returns additional entries for IncludedInInfo as multimap where the key is the row title and    * the the values are a list of systems that include the given commit (e.g. names of servers on    * which this commit is deployed).    *    *<p>The tags and branches in which the commit is included are provided so that a RevWalk can be    * avoided when a system runs a certain tag or branch.    *    * @param project the name of the project    * @param commit the ID of the commit for which it should be checked if it is included    * @param tags the tags that include the commit    * @param branches the branches that include the commit    * @return additional entries for IncludedInInfo    */
DECL|method|getIncludedIn ( String project, String commit, Collection<String> tags, Collection<String> branches)
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getIncludedIn
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|commit
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|tags
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|branches
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

