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
DECL|package|com.google.gerrit.entities
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
package|;
end_package

begin_comment
comment|/**  * Contains all inheritable boolean project configs and maps internal representations to API  * objects.  *  *<p>Perform the following steps for adding a new inheritable boolean project config:  *  *<ol>  *<li>Add a field to {@link com.google.gerrit.extensions.api.projects.ConfigInput}  *<li>Add a field to {@link com.google.gerrit.extensions.api.projects.ConfigInfo}  *<li>Add the config to this enum  *<li>Add API mappers to {@link  *       com.google.gerrit.server.project.BooleanProjectConfigTransformations}  *</ol>  */
end_comment

begin_enum
DECL|enum|BooleanProjectConfig
specifier|public
enum|enum
name|BooleanProjectConfig
block|{
DECL|enumConstant|USE_CONTRIBUTOR_AGREEMENTS
name|USE_CONTRIBUTOR_AGREEMENTS
argument_list|(
literal|"receive"
argument_list|,
literal|"requireContributorAgreement"
argument_list|)
block|,
DECL|enumConstant|USE_SIGNED_OFF_BY
name|USE_SIGNED_OFF_BY
argument_list|(
literal|"receive"
argument_list|,
literal|"requireSignedOffBy"
argument_list|)
block|,
DECL|enumConstant|USE_CONTENT_MERGE
name|USE_CONTENT_MERGE
argument_list|(
literal|"submit"
argument_list|,
literal|"mergeContent"
argument_list|)
block|,
DECL|enumConstant|REQUIRE_CHANGE_ID
name|REQUIRE_CHANGE_ID
argument_list|(
literal|"receive"
argument_list|,
literal|"requireChangeId"
argument_list|)
block|,
DECL|enumConstant|CREATE_NEW_CHANGE_FOR_ALL_NOT_IN_TARGET
name|CREATE_NEW_CHANGE_FOR_ALL_NOT_IN_TARGET
argument_list|(
literal|"receive"
argument_list|,
literal|"createNewChangeForAllNotInTarget"
argument_list|)
block|,
DECL|enumConstant|ENABLE_SIGNED_PUSH
name|ENABLE_SIGNED_PUSH
argument_list|(
literal|"receive"
argument_list|,
literal|"enableSignedPush"
argument_list|)
block|,
DECL|enumConstant|REQUIRE_SIGNED_PUSH
name|REQUIRE_SIGNED_PUSH
argument_list|(
literal|"receive"
argument_list|,
literal|"requireSignedPush"
argument_list|)
block|,
DECL|enumConstant|REJECT_IMPLICIT_MERGES
name|REJECT_IMPLICIT_MERGES
argument_list|(
literal|"receive"
argument_list|,
literal|"rejectImplicitMerges"
argument_list|)
block|,
DECL|enumConstant|PRIVATE_BY_DEFAULT
name|PRIVATE_BY_DEFAULT
argument_list|(
literal|"change"
argument_list|,
literal|"privateByDefault"
argument_list|)
block|,
DECL|enumConstant|ENABLE_REVIEWER_BY_EMAIL
name|ENABLE_REVIEWER_BY_EMAIL
argument_list|(
literal|"reviewer"
argument_list|,
literal|"enableByEmail"
argument_list|)
block|,
DECL|enumConstant|MATCH_AUTHOR_TO_COMMITTER_DATE
name|MATCH_AUTHOR_TO_COMMITTER_DATE
argument_list|(
literal|"submit"
argument_list|,
literal|"matchAuthorToCommitterDate"
argument_list|)
block|,
DECL|enumConstant|REJECT_EMPTY_COMMIT
name|REJECT_EMPTY_COMMIT
argument_list|(
literal|"submit"
argument_list|,
literal|"rejectEmptyCommit"
argument_list|)
block|,
DECL|enumConstant|WORK_IN_PROGRESS_BY_DEFAULT
name|WORK_IN_PROGRESS_BY_DEFAULT
argument_list|(
literal|"change"
argument_list|,
literal|"workInProgressByDefault"
argument_list|)
block|;
comment|// Git config
DECL|field|section
specifier|private
specifier|final
name|String
name|section
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|BooleanProjectConfig (String section, String name)
name|BooleanProjectConfig
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|section
operator|=
name|section
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getSection ()
specifier|public
name|String
name|getSection
parameter_list|()
block|{
return|return
name|section
return|;
block|}
DECL|method|getSubSection ()
specifier|public
name|String
name|getSubSection
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
end_enum

end_unit

