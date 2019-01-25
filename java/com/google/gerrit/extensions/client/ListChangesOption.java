begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Output options available for retrieval of change details. */
end_comment

begin_enum
DECL|enum|ListChangesOption
specifier|public
enum|enum
name|ListChangesOption
block|{
DECL|enumConstant|LABELS
name|LABELS
argument_list|(
literal|0
argument_list|)
block|,
DECL|enumConstant|DETAILED_LABELS
name|DETAILED_LABELS
argument_list|(
literal|8
argument_list|)
block|,
comment|/** Return information on the current patch set of the change. */
DECL|enumConstant|CURRENT_REVISION
name|CURRENT_REVISION
argument_list|(
literal|1
argument_list|)
block|,
DECL|enumConstant|ALL_REVISIONS
name|ALL_REVISIONS
argument_list|(
literal|2
argument_list|)
block|,
comment|/** If revisions are included, parse the commit object. */
DECL|enumConstant|CURRENT_COMMIT
name|CURRENT_COMMIT
argument_list|(
literal|3
argument_list|)
block|,
DECL|enumConstant|ALL_COMMITS
name|ALL_COMMITS
argument_list|(
literal|4
argument_list|)
block|,
comment|/** If a patch set is included, include the files of the patch set. */
DECL|enumConstant|CURRENT_FILES
name|CURRENT_FILES
argument_list|(
literal|5
argument_list|)
block|,
DECL|enumConstant|ALL_FILES
name|ALL_FILES
argument_list|(
literal|6
argument_list|)
block|,
comment|/** If accounts are included, include detailed account info. */
DECL|enumConstant|DETAILED_ACCOUNTS
name|DETAILED_ACCOUNTS
argument_list|(
literal|7
argument_list|)
block|,
comment|/** Include messages associated with the change. */
DECL|enumConstant|MESSAGES
name|MESSAGES
argument_list|(
literal|9
argument_list|)
block|,
comment|/** Include allowed actions client could perform. */
DECL|enumConstant|CURRENT_ACTIONS
name|CURRENT_ACTIONS
argument_list|(
literal|10
argument_list|)
block|,
comment|/** Set the reviewed boolean for the caller. */
DECL|enumConstant|REVIEWED
name|REVIEWED
argument_list|(
literal|11
argument_list|)
block|,
comment|/** Not used anymore, kept for backward compatibility */
DECL|enumConstant|Deprecated
annotation|@
name|Deprecated
DECL|enumConstant|DRAFT_COMMENTS
name|DRAFT_COMMENTS
argument_list|(
literal|12
argument_list|)
block|,
comment|/** Include download commands for the caller. */
DECL|enumConstant|DOWNLOAD_COMMANDS
name|DOWNLOAD_COMMANDS
argument_list|(
literal|13
argument_list|)
block|,
comment|/** Include patch set weblinks. */
DECL|enumConstant|WEB_LINKS
name|WEB_LINKS
argument_list|(
literal|14
argument_list|)
block|,
comment|/** Include consistency check results. */
DECL|enumConstant|CHECK
name|CHECK
argument_list|(
literal|15
argument_list|)
block|,
comment|/** Include allowed change actions client could perform. */
DECL|enumConstant|CHANGE_ACTIONS
name|CHANGE_ACTIONS
argument_list|(
literal|16
argument_list|)
block|,
comment|/** Include a copy of commit messages including review footers. */
DECL|enumConstant|COMMIT_FOOTERS
name|COMMIT_FOOTERS
argument_list|(
literal|17
argument_list|)
block|,
comment|/** Include push certificate information along with any patch sets. */
DECL|enumConstant|PUSH_CERTIFICATES
name|PUSH_CERTIFICATES
argument_list|(
literal|18
argument_list|)
block|,
comment|/** Include change's reviewer updates. */
DECL|enumConstant|REVIEWER_UPDATES
name|REVIEWER_UPDATES
argument_list|(
literal|19
argument_list|)
block|,
comment|/** Set the submittable boolean. */
DECL|enumConstant|SUBMITTABLE
name|SUBMITTABLE
argument_list|(
literal|20
argument_list|)
block|,
comment|/** If tracking Ids are included, include detailed tracking Ids info. */
DECL|enumConstant|TRACKING_IDS
name|TRACKING_IDS
argument_list|(
literal|21
argument_list|)
block|,
comment|/** Skip mergeability data */
DECL|enumConstant|Deprecated
annotation|@
name|Deprecated
DECL|enumConstant|SKIP_MERGEABLE
name|SKIP_MERGEABLE
argument_list|(
literal|22
argument_list|)
decl_stmt|;
DECL|field|value
specifier|private
specifier|final
name|int
name|value
decl_stmt|;
DECL|method|ListChangesOption (int v)
name|ListChangesOption
parameter_list|(
name|int
name|v
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|v
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|int
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|fromBits (int v)
specifier|public
specifier|static
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|fromBits
parameter_list|(
name|int
name|v
parameter_list|)
block|{
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|r
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListChangesOption
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|ListChangesOption
name|o
range|:
name|ListChangesOption
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
name|v
operator|&
operator|(
literal|1
operator|<<
name|o
operator|.
name|value
operator|)
operator|)
operator|!=
literal|0
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|v
operator|&=
operator|~
operator|(
literal|1
operator|<<
name|o
operator|.
name|value
operator|)
expr_stmt|;
block|}
if|if
condition|(
name|v
operator|==
literal|0
condition|)
block|{
return|return
name|r
return|;
block|}
block|}
if|if
condition|(
name|v
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown "
operator|+
name|Integer
operator|.
name|toHexString
argument_list|(
name|v
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|r
return|;
block|}
DECL|method|toBits (Set<ListChangesOption> set)
specifier|public
specifier|static
name|int
name|toBits
parameter_list|(
name|Set
argument_list|<
name|ListChangesOption
argument_list|>
name|set
parameter_list|)
block|{
name|int
name|r
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ListChangesOption
name|o
range|:
name|set
control|)
block|{
name|r
operator||=
literal|1
operator|<<
name|o
operator|.
name|value
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
block|}
end_enum

end_unit

