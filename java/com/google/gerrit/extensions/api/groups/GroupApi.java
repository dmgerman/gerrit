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
DECL|package|com.google.gerrit.extensions.api.groups
package|package
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
name|groups
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
name|extensions
operator|.
name|common
operator|.
name|AccountInfo
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
name|common
operator|.
name|GroupAuditEventInfo
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
name|common
operator|.
name|GroupInfo
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
name|common
operator|.
name|GroupOptionsInfo
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
name|restapi
operator|.
name|NotImplementedException
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
name|restapi
operator|.
name|RestApiException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_interface
DECL|interface|GroupApi
specifier|public
interface|interface
name|GroupApi
block|{
comment|/** @return group info with no {@code ListGroupsOption}s set. */
DECL|method|get ()
name|GroupInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** @return group info with all {@code ListGroupsOption}s set. */
DECL|method|detail ()
name|GroupInfo
name|detail
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** @return group name. */
DECL|method|name ()
name|String
name|name
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Set group name.    *    * @param name new name.    * @throws RestApiException    */
DECL|method|name (String name)
name|void
name|name
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** @return owning group info. */
DECL|method|owner ()
name|GroupInfo
name|owner
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Set group owner.    *    * @param owner identifier of new group owner.    * @throws RestApiException    */
DECL|method|owner (String owner)
name|void
name|owner
parameter_list|(
name|String
name|owner
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** @return group description. */
DECL|method|description ()
name|String
name|description
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Set group decsription.    *    * @param description new description.    * @throws RestApiException    */
DECL|method|description (String description)
name|void
name|description
parameter_list|(
name|String
name|description
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** @return group options. */
DECL|method|options ()
name|GroupOptionsInfo
name|options
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Set group options.    *    * @param options new options.    * @throws RestApiException    */
DECL|method|options (GroupOptionsInfo options)
name|void
name|options
parameter_list|(
name|GroupOptionsInfo
name|options
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * List group members, non-recursively.    *    * @return group members.    * @throws RestApiException    */
DECL|method|members ()
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|members
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * List group members.    *    * @param recursive whether to recursively included groups.    * @return group members.    * @throws RestApiException    */
DECL|method|members (boolean recursive)
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|members
parameter_list|(
name|boolean
name|recursive
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Add members to a group.    *    * @param members list of member identifiers, in any format accepted by {@link    *     com.google.gerrit.extensions.api.accounts.Accounts#id(String)}    * @throws RestApiException    */
DECL|method|addMembers (List<String> members)
name|void
name|addMembers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|members
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Add members to a group.    *    * @param members list of member identifiers, in any format accepted by {@link    *     com.google.gerrit.extensions.api.accounts.Accounts#id(String)}    * @throws RestApiException    */
DECL|method|addMembers (String... members)
specifier|default
name|void
name|addMembers
parameter_list|(
name|String
modifier|...
name|members
parameter_list|)
throws|throws
name|RestApiException
block|{
name|addMembers
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|members
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Remove members from a group.    *    * @param members list of member identifiers, in any format accepted by {@link    *     com.google.gerrit.extensions.api.accounts.Accounts#id(String)}    * @throws RestApiException    */
DECL|method|removeMembers (List<String> members)
name|void
name|removeMembers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|members
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Remove members from a group.    *    * @param members list of member identifiers, in any format accepted by {@link    *     com.google.gerrit.extensions.api.accounts.Accounts#id(String)}    * @throws RestApiException    */
DECL|method|removeMembers (String... members)
specifier|default
name|void
name|removeMembers
parameter_list|(
name|String
modifier|...
name|members
parameter_list|)
throws|throws
name|RestApiException
block|{
name|removeMembers
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|members
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Lists the subgroups of this group.    *    * @return the found subgroups    * @throws RestApiException    */
DECL|method|includedGroups ()
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|includedGroups
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Adds subgroups to this group.    *    * @param groups list of group identifiers, in any format accepted by {@link Groups#id(String)}    * @throws RestApiException    */
DECL|method|addGroups (List<String> groups)
name|void
name|addGroups
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Adds subgroups to this group.    *    * @param groups list of group identifiers, in any format accepted by {@link Groups#id(String)}    * @throws RestApiException    */
DECL|method|addGroups (String... groups)
specifier|default
name|void
name|addGroups
parameter_list|(
name|String
modifier|...
name|groups
parameter_list|)
throws|throws
name|RestApiException
block|{
name|addGroups
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|groups
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Removes subgroups from this group.    *    * @param groups list of group identifiers, in any format accepted by {@link Groups#id(String)}    * @throws RestApiException    */
DECL|method|removeGroups (List<String> groups)
name|void
name|removeGroups
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Removes subgroups from this group.    *    * @param groups list of group identifiers, in any format accepted by {@link Groups#id(String)}    * @throws RestApiException    */
DECL|method|removeGroups (String... groups)
specifier|default
name|void
name|removeGroups
parameter_list|(
name|String
modifier|...
name|groups
parameter_list|)
throws|throws
name|RestApiException
block|{
name|removeGroups
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|groups
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the audit log of the group.    *    * @return list of audit events of the group.    * @throws RestApiException    */
DECL|method|auditLog ()
name|List
argument_list|<
name|?
extends|extends
name|GroupAuditEventInfo
argument_list|>
name|auditLog
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Reindexes the group.    *    *<p>Only supported for internal groups.    *    * @throws RestApiException    */
DECL|method|index ()
name|void
name|index
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * A default implementation which allows source compatibility when adding new methods to the    * interface.    */
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|GroupApi
block|{
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|GroupInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|detail ()
specifier|public
name|GroupInfo
name|detail
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|name ()
specifier|public
name|String
name|name
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|name (String name)
specifier|public
name|void
name|name
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|owner ()
specifier|public
name|GroupInfo
name|owner
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|owner (String owner)
specifier|public
name|void
name|owner
parameter_list|(
name|String
name|owner
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|description ()
specifier|public
name|String
name|description
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|description (String description)
specifier|public
name|void
name|description
parameter_list|(
name|String
name|description
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|options ()
specifier|public
name|GroupOptionsInfo
name|options
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|options (GroupOptionsInfo options)
specifier|public
name|void
name|options
parameter_list|(
name|GroupOptionsInfo
name|options
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|members ()
specifier|public
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|members
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|members (boolean recursive)
specifier|public
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|members
parameter_list|(
name|boolean
name|recursive
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|addMembers (List<String> members)
specifier|public
name|void
name|addMembers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|members
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|removeMembers (List<String> members)
specifier|public
name|void
name|removeMembers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|members
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|includedGroups ()
specifier|public
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|includedGroups
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|addGroups (List<String> groups)
specifier|public
name|void
name|addGroups
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|removeGroups (List<String> groups)
specifier|public
name|void
name|removeGroups
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|auditLog ()
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|GroupAuditEventInfo
argument_list|>
name|auditLog
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|index ()
specifier|public
name|void
name|index
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
block|}
block|}
end_interface

end_unit

