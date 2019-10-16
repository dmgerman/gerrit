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
DECL|package|com.google.gerrit.acceptance.testsuite.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
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
name|gerrit
operator|.
name|entities
operator|.
name|Project
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
name|project
operator|.
name|ProjectConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_comment
comment|/**  * Operations for constructing projects in tests. This does not necessarily use the project REST  * API, so don't use it for testing that.  */
end_comment

begin_interface
DECL|interface|ProjectOperations
specifier|public
interface|interface
name|ProjectOperations
block|{
comment|/** Starts a fluent chain for creating a new project. */
DECL|method|newProject ()
name|TestProjectCreation
operator|.
name|Builder
name|newProject
parameter_list|()
function_decl|;
DECL|method|project (Project.NameKey key)
name|PerProjectOperations
name|project
parameter_list|(
name|Project
operator|.
name|NameKey
name|key
parameter_list|)
function_decl|;
comment|/** Starts a fluent chain for updating All-Projects. */
DECL|method|allProjectsForUpdate ()
name|TestProjectUpdate
operator|.
name|Builder
name|allProjectsForUpdate
parameter_list|()
function_decl|;
DECL|interface|PerProjectOperations
interface|interface
name|PerProjectOperations
block|{
comment|/**      * Returns the commit for this project. branchName can either be shortened ("HEAD", "master") or      * a fully qualified refname ("refs/heads/master"). The branch must exist.      */
DECL|method|getHead (String branchName)
name|RevCommit
name|getHead
parameter_list|(
name|String
name|branchName
parameter_list|)
function_decl|;
comment|/**      * Returns true if a branch exists. branchName can either be shortened ("HEAD", "master") or a      * fully qualified refname ("refs/heads/master").      */
DECL|method|hasHead (String branchName)
name|boolean
name|hasHead
parameter_list|(
name|String
name|branchName
parameter_list|)
function_decl|;
comment|/** Returns a fresh {@link ProjectConfig} read from the tip of {@code refs/meta/config}. */
DECL|method|getProjectConfig ()
name|ProjectConfig
name|getProjectConfig
parameter_list|()
function_decl|;
comment|/**      * Returns a fresh JGit {@link Config} instance read from {@code project.config} at the tip of      * {@code refs/meta/config}. Does not have a base config, i.e. does not respect {@code      * $site_path/etc/project.config}.      */
DECL|method|getConfig ()
name|Config
name|getConfig
parameter_list|()
function_decl|;
comment|/**      * Starts the fluent chain to update a project. The returned builder can be used to specify how      * the attributes of the project should be modified. To update the project for real, the {@link      * TestProjectUpdate.Builder#update()} must be called.      *      *<p>Example:      *      *<pre>      * projectOperations      *     .forUpdate()      *     .add(allow(ABANDON).ref("refs/*").group(REGISTERED_USERS))      *     .update();      *</pre>      *      * @return a builder to update the check.      */
DECL|method|forUpdate ()
name|TestProjectUpdate
operator|.
name|Builder
name|forUpdate
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit

