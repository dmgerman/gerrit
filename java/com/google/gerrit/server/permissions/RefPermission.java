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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|api
operator|.
name|access
operator|.
name|GerritPermission
import|;
end_import

begin_enum
DECL|enum|RefPermission
specifier|public
enum|enum
name|RefPermission
implements|implements
name|GerritPermission
block|{
DECL|enumConstant|READ
name|READ
block|,
DECL|enumConstant|CREATE
name|CREATE
block|,
comment|/**    * Before checking this permission, the caller needs to verify the branch is deletable and reject    * early if the branch should never be deleted. For example, the refs/meta/config branch should    * never be deleted because deleting this branch would destroy all Gerrit specific metadata about    * the project, including its access rules. If a project is to be removed from Gerrit, its    * repository should be removed first.    */
DECL|enumConstant|DELETE
name|DELETE
block|,
DECL|enumConstant|UPDATE
name|UPDATE
block|,
DECL|enumConstant|FORCE_UPDATE
name|FORCE_UPDATE
block|,
DECL|enumConstant|SET_HEAD
name|SET_HEAD
argument_list|(
literal|"set HEAD"
argument_list|)
block|,
DECL|enumConstant|FORGE_AUTHOR
name|FORGE_AUTHOR
block|,
DECL|enumConstant|FORGE_COMMITTER
name|FORGE_COMMITTER
block|,
DECL|enumConstant|FORGE_SERVER
name|FORGE_SERVER
block|,
DECL|enumConstant|MERGE
name|MERGE
block|,
comment|/**    * Before checking this permission, the caller should verify {@code USE_SIGNED_OFF_BY} is false.    * If it's true, the request should be rejected directly without further check this permission.    */
DECL|enumConstant|SKIP_VALIDATION
name|SKIP_VALIDATION
block|,
comment|/** Create a change to code review a commit. */
DECL|enumConstant|CREATE_CHANGE
name|CREATE_CHANGE
block|,
comment|/** Create a tag. */
DECL|enumConstant|CREATE_TAG
name|CREATE_TAG
block|,
comment|/** Create a signed tag. */
DECL|enumConstant|CREATE_SIGNED_TAG
name|CREATE_SIGNED_TAG
block|,
comment|/**    * Creates changes, then also immediately submits them during {@code push}.    *    *<p>This is similar to {@link #UPDATE} except it constructs changes first, then submits them    * according to the submit strategy, which may include cherry-pick or rebase. By creating changes    * for each commit, automatic server side rebase, and post-update review are enabled.    */
DECL|enumConstant|UPDATE_BY_SUBMIT
name|UPDATE_BY_SUBMIT
block|,
comment|/**    * Can read all private changes on the ref. Typically granted to CI systems if they should run on    * private changes.    */
DECL|enumConstant|READ_PRIVATE_CHANGES
name|READ_PRIVATE_CHANGES
block|,
comment|/** Read access to ref's config section in {@code project.config}. */
DECL|enumConstant|READ_CONFIG
name|READ_CONFIG
argument_list|(
literal|"read ref config"
argument_list|)
block|,
comment|/** Write access to ref's config section in {@code project.config}. */
DECL|enumConstant|WRITE_CONFIG
name|WRITE_CONFIG
argument_list|(
literal|"write ref config"
argument_list|)
block|;
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|method|RefPermission ()
name|RefPermission
parameter_list|()
block|{
name|this
operator|.
name|description
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|RefPermission (String description)
name|RefPermission
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|requireNonNull
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|describeForException ()
specifier|public
name|String
name|describeForException
parameter_list|()
block|{
return|return
name|description
operator|!=
literal|null
condition|?
name|description
else|:
name|GerritPermission
operator|.
name|describeEnumValue
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

