begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
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
name|ChangeInfo
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
operator|.
name|Status
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
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
name|reviewdb
operator|.
name|client
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
name|gwtorm
operator|.
name|client
operator|.
name|KeyUtil
import|;
end_import

begin_class
DECL|class|PageLinks
specifier|public
class|class
name|PageLinks
block|{
DECL|field|SETTINGS
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS
init|=
literal|"/settings/"
decl_stmt|;
DECL|field|SETTINGS_PREFERENCES
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS_PREFERENCES
init|=
literal|"/settings/preferences"
decl_stmt|;
DECL|field|SETTINGS_SSHKEYS
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS_SSHKEYS
init|=
literal|"/settings/ssh-keys"
decl_stmt|;
DECL|field|SETTINGS_HTTP_PASSWORD
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS_HTTP_PASSWORD
init|=
literal|"/settings/http-password"
decl_stmt|;
DECL|field|SETTINGS_WEBIDENT
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS_WEBIDENT
init|=
literal|"/settings/web-identities"
decl_stmt|;
DECL|field|SETTINGS_MYGROUPS
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS_MYGROUPS
init|=
literal|"/settings/group-memberships"
decl_stmt|;
DECL|field|SETTINGS_AGREEMENTS
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS_AGREEMENTS
init|=
literal|"/settings/agreements"
decl_stmt|;
DECL|field|SETTINGS_CONTACT
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS_CONTACT
init|=
literal|"/settings/contact"
decl_stmt|;
DECL|field|SETTINGS_PROJECTS
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS_PROJECTS
init|=
literal|"/settings/projects"
decl_stmt|;
DECL|field|SETTINGS_NEW_AGREEMENT
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS_NEW_AGREEMENT
init|=
literal|"/settings/new-agreement"
decl_stmt|;
DECL|field|REGISTER
specifier|public
specifier|static
specifier|final
name|String
name|REGISTER
init|=
literal|"/register"
decl_stmt|;
DECL|field|TOP
specifier|public
specifier|static
specifier|final
name|String
name|TOP
init|=
literal|"n,z"
decl_stmt|;
DECL|field|MINE
specifier|public
specifier|static
specifier|final
name|String
name|MINE
init|=
literal|"/"
decl_stmt|;
DECL|field|PROJECTS
specifier|public
specifier|static
specifier|final
name|String
name|PROJECTS
init|=
literal|"/projects/"
decl_stmt|;
DECL|field|DASHBOARDS
specifier|public
specifier|static
specifier|final
name|String
name|DASHBOARDS
init|=
literal|",dashboards/"
decl_stmt|;
DECL|field|ADMIN_GROUPS
specifier|public
specifier|static
specifier|final
name|String
name|ADMIN_GROUPS
init|=
literal|"/admin/groups/"
decl_stmt|;
DECL|field|ADMIN_CREATE_GROUP
specifier|public
specifier|static
specifier|final
name|String
name|ADMIN_CREATE_GROUP
init|=
literal|"/admin/create-group/"
decl_stmt|;
DECL|field|ADMIN_PROJECTS
specifier|public
specifier|static
specifier|final
name|String
name|ADMIN_PROJECTS
init|=
literal|"/admin/projects/"
decl_stmt|;
DECL|field|ADMIN_CREATE_PROJECT
specifier|public
specifier|static
specifier|final
name|String
name|ADMIN_CREATE_PROJECT
init|=
literal|"/admin/create-project/"
decl_stmt|;
DECL|field|ADMIN_PLUGINS
specifier|public
specifier|static
specifier|final
name|String
name|ADMIN_PLUGINS
init|=
literal|"/admin/plugins/"
decl_stmt|;
DECL|method|toChange (final ChangeInfo c)
specifier|public
specifier|static
name|String
name|toChange
parameter_list|(
specifier|final
name|ChangeInfo
name|c
parameter_list|)
block|{
return|return
name|toChange
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|toChange (final Change.Id c)
specifier|public
specifier|static
name|String
name|toChange
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|c
parameter_list|)
block|{
return|return
literal|"/c/"
operator|+
name|c
operator|+
literal|"/"
return|;
block|}
DECL|method|toChange (Change.Id c, String p)
specifier|public
specifier|static
name|String
name|toChange
parameter_list|(
name|Change
operator|.
name|Id
name|c
parameter_list|,
name|String
name|p
parameter_list|)
block|{
return|return
literal|"/c/"
operator|+
name|c
operator|+
literal|"/"
operator|+
name|p
return|;
block|}
DECL|method|toChange (final PatchSet.Id ps)
specifier|public
specifier|static
name|String
name|toChange
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|ps
parameter_list|)
block|{
return|return
literal|"/c/"
operator|+
name|ps
operator|.
name|getParentKey
argument_list|()
operator|+
literal|"/"
operator|+
name|ps
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|toProject (final Project.NameKey p)
specifier|public
specifier|static
name|String
name|toProject
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|p
parameter_list|)
block|{
return|return
name|ADMIN_PROJECTS
operator|+
name|p
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|toProjectAcceess (final Project.NameKey p)
specifier|public
specifier|static
name|String
name|toProjectAcceess
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|p
parameter_list|)
block|{
return|return
literal|"/admin/projects/"
operator|+
name|p
operator|.
name|get
argument_list|()
operator|+
literal|",access"
return|;
block|}
DECL|method|toProjectBranches (Project.NameKey p)
specifier|public
specifier|static
name|String
name|toProjectBranches
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|)
block|{
return|return
literal|"/admin/projects/"
operator|+
name|p
operator|.
name|get
argument_list|()
operator|+
literal|",branches"
return|;
block|}
DECL|method|toAccountQuery (String fullname, Status status)
specifier|public
specifier|static
name|String
name|toAccountQuery
parameter_list|(
name|String
name|fullname
parameter_list|,
name|Status
name|status
parameter_list|)
block|{
return|return
name|toChangeQuery
argument_list|(
name|op
argument_list|(
literal|"owner"
argument_list|,
name|fullname
argument_list|)
operator|+
literal|" "
operator|+
name|status
argument_list|(
name|status
argument_list|)
argument_list|,
name|TOP
argument_list|)
return|;
block|}
DECL|method|toCustomDashboard (final String params)
specifier|public
specifier|static
name|String
name|toCustomDashboard
parameter_list|(
specifier|final
name|String
name|params
parameter_list|)
block|{
return|return
literal|"/dashboard/?"
operator|+
name|params
return|;
block|}
DECL|method|toProjectDashboards (Project.NameKey proj)
specifier|public
specifier|static
name|String
name|toProjectDashboards
parameter_list|(
name|Project
operator|.
name|NameKey
name|proj
parameter_list|)
block|{
return|return
name|ADMIN_PROJECTS
operator|+
name|proj
operator|.
name|get
argument_list|()
operator|+
literal|",dashboards"
return|;
block|}
DECL|method|toChangeQuery (final String query)
specifier|public
specifier|static
name|String
name|toChangeQuery
parameter_list|(
specifier|final
name|String
name|query
parameter_list|)
block|{
return|return
name|toChangeQuery
argument_list|(
name|query
argument_list|,
name|TOP
argument_list|)
return|;
block|}
DECL|method|toChangeQuery (String query, String page)
specifier|public
specifier|static
name|String
name|toChangeQuery
parameter_list|(
name|String
name|query
parameter_list|,
name|String
name|page
parameter_list|)
block|{
return|return
literal|"/q/"
operator|+
name|KeyUtil
operator|.
name|encode
argument_list|(
name|query
argument_list|)
operator|+
literal|","
operator|+
name|page
return|;
block|}
DECL|method|toProjectDashboard (Project.NameKey name, String id)
specifier|public
specifier|static
name|String
name|toProjectDashboard
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|,
name|String
name|id
parameter_list|)
block|{
return|return
name|PROJECTS
operator|+
name|name
operator|.
name|get
argument_list|()
operator|+
name|DASHBOARDS
operator|+
name|id
return|;
block|}
DECL|method|toProjectDefaultDashboard (Project.NameKey name)
specifier|public
specifier|static
name|String
name|toProjectDefaultDashboard
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
return|return
name|PROJECTS
operator|+
name|name
operator|.
name|get
argument_list|()
operator|+
name|DASHBOARDS
operator|+
literal|"default"
return|;
block|}
DECL|method|projectQuery (Project.NameKey proj)
specifier|public
specifier|static
name|String
name|projectQuery
parameter_list|(
name|Project
operator|.
name|NameKey
name|proj
parameter_list|)
block|{
return|return
name|op
argument_list|(
literal|"project"
argument_list|,
name|proj
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|projectQuery (Project.NameKey proj, Status status)
specifier|public
specifier|static
name|String
name|projectQuery
parameter_list|(
name|Project
operator|.
name|NameKey
name|proj
parameter_list|,
name|Status
name|status
parameter_list|)
block|{
return|return
name|status
argument_list|(
name|status
argument_list|)
operator|+
literal|" "
operator|+
name|op
argument_list|(
literal|"project"
argument_list|,
name|proj
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|toGroup (AccountGroup.UUID uuid)
specifier|public
specifier|static
name|String
name|toGroup
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
return|return
name|ADMIN_GROUPS
operator|+
literal|"uuid-"
operator|+
name|uuid
return|;
block|}
DECL|method|status (Status status)
specifier|private
specifier|static
name|String
name|status
parameter_list|(
name|Status
name|status
parameter_list|)
block|{
switch|switch
condition|(
name|status
condition|)
block|{
case|case
name|ABANDONED
case|:
return|return
literal|"status:abandoned"
return|;
case|case
name|MERGED
case|:
return|return
literal|"status:merged"
return|;
case|case
name|NEW
case|:
case|case
name|SUBMITTED
case|:
default|default:
return|return
literal|"status:open"
return|;
block|}
block|}
DECL|method|op (String op, int value)
specifier|public
specifier|static
name|String
name|op
parameter_list|(
name|String
name|op
parameter_list|,
name|int
name|value
parameter_list|)
block|{
return|return
name|op
operator|+
literal|":"
operator|+
name|value
return|;
block|}
DECL|method|op (String op, String value)
specifier|public
specifier|static
name|String
name|op
parameter_list|(
name|String
name|op
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|isSingleWord
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|op
operator|+
literal|":"
operator|+
name|value
return|;
block|}
return|return
name|op
operator|+
literal|":\""
operator|+
name|value
operator|+
literal|"\""
return|;
block|}
DECL|method|isSingleWord (String value)
specifier|private
specifier|static
name|boolean
name|isSingleWord
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|value
operator|.
name|matches
argument_list|(
literal|"[^\u0000-\u0020!\"#$%&'():;?\\[\\]{}~]+"
argument_list|)
return|;
block|}
DECL|method|PageLinks ()
specifier|protected
name|PageLinks
parameter_list|()
block|{   }
block|}
end_class

end_unit

