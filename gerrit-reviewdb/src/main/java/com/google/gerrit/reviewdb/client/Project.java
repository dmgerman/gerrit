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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
package|;
end_package

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
name|Column
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
name|StringKey
import|;
end_import

begin_comment
comment|/** Projects match a source code repository managed by Gerrit */
end_comment

begin_class
DECL|class|Project
specifier|public
specifier|final
class|class
name|Project
block|{
comment|/** Project name key */
DECL|class|NameKey
specifier|public
specifier|static
class|class
name|NameKey
extends|extends
name|StringKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|name
specifier|protected
name|String
name|name
decl_stmt|;
DECL|method|NameKey ()
specifier|protected
name|NameKey
parameter_list|()
block|{     }
DECL|method|NameKey (final String n)
specifier|public
name|NameKey
parameter_list|(
specifier|final
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|set (String newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|name
operator|=
name|newValue
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object b)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
operator|instanceof
name|NameKey
condition|)
block|{
return|return
name|get
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|NameKey
operator|)
name|b
operator|)
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/** Parse a Project.NameKey out of a string representation. */
DECL|method|parse (final String str)
specifier|public
specifier|static
name|NameKey
name|parse
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
specifier|final
name|NameKey
name|r
init|=
operator|new
name|NameKey
argument_list|()
decl_stmt|;
name|r
operator|.
name|fromString
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
DECL|enum|SubmitType
specifier|public
specifier|static
enum|enum
name|SubmitType
block|{
DECL|enumConstant|FAST_FORWARD_ONLY
name|FAST_FORWARD_ONLY
block|,
DECL|enumConstant|MERGE_IF_NECESSARY
name|MERGE_IF_NECESSARY
block|,
DECL|enumConstant|REBASE_IF_NECESSARY
name|REBASE_IF_NECESSARY
block|,
DECL|enumConstant|MERGE_ALWAYS
name|MERGE_ALWAYS
block|,
DECL|enumConstant|CHERRY_PICK
name|CHERRY_PICK
block|;   }
DECL|enum|State
specifier|public
specifier|static
enum|enum
name|State
block|{
DECL|enumConstant|ACTIVE
name|ACTIVE
block|,
DECL|enumConstant|READ_ONLY
name|READ_ONLY
block|,
DECL|enumConstant|HIDDEN
name|HIDDEN
block|;   }
DECL|enum|InheritableBoolean
specifier|public
specifier|static
enum|enum
name|InheritableBoolean
block|{
DECL|enumConstant|TRUE
name|TRUE
block|,
DECL|enumConstant|FALSE
name|FALSE
block|,
DECL|enumConstant|INHERIT
name|INHERIT
block|;   }
DECL|field|name
specifier|protected
name|NameKey
name|name
decl_stmt|;
DECL|field|description
specifier|protected
name|String
name|description
decl_stmt|;
DECL|field|useContributorAgreements
specifier|protected
name|InheritableBoolean
name|useContributorAgreements
decl_stmt|;
DECL|field|useSignedOffBy
specifier|protected
name|InheritableBoolean
name|useSignedOffBy
decl_stmt|;
DECL|field|submitType
specifier|protected
name|SubmitType
name|submitType
decl_stmt|;
DECL|field|state
specifier|protected
name|State
name|state
decl_stmt|;
DECL|field|parent
specifier|protected
name|NameKey
name|parent
decl_stmt|;
DECL|field|requireChangeID
specifier|protected
name|InheritableBoolean
name|requireChangeID
decl_stmt|;
DECL|field|useContentMerge
specifier|protected
name|InheritableBoolean
name|useContentMerge
decl_stmt|;
DECL|field|defaultDashboardId
specifier|protected
name|String
name|defaultDashboardId
decl_stmt|;
DECL|field|localDefaultDashboardId
specifier|protected
name|String
name|localDefaultDashboardId
decl_stmt|;
DECL|field|themeName
specifier|protected
name|String
name|themeName
decl_stmt|;
DECL|method|Project ()
specifier|protected
name|Project
parameter_list|()
block|{   }
DECL|method|Project (Project.NameKey nameKey)
specifier|public
name|Project
parameter_list|(
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
block|{
name|name
operator|=
name|nameKey
expr_stmt|;
name|submitType
operator|=
name|SubmitType
operator|.
name|MERGE_IF_NECESSARY
expr_stmt|;
name|state
operator|=
name|State
operator|.
name|ACTIVE
expr_stmt|;
name|useContributorAgreements
operator|=
name|InheritableBoolean
operator|.
name|INHERIT
expr_stmt|;
name|useSignedOffBy
operator|=
name|InheritableBoolean
operator|.
name|INHERIT
expr_stmt|;
name|requireChangeID
operator|=
name|InheritableBoolean
operator|.
name|INHERIT
expr_stmt|;
name|useContentMerge
operator|=
name|InheritableBoolean
operator|.
name|INHERIT
expr_stmt|;
block|}
DECL|method|getNameKey ()
specifier|public
name|Project
operator|.
name|NameKey
name|getNameKey
parameter_list|()
block|{
return|return
name|name
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
operator|!=
literal|null
condition|?
name|name
operator|.
name|get
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (final String d)
specifier|public
name|void
name|setDescription
parameter_list|(
specifier|final
name|String
name|d
parameter_list|)
block|{
name|description
operator|=
name|d
expr_stmt|;
block|}
DECL|method|getUseContributorAgreements ()
specifier|public
name|InheritableBoolean
name|getUseContributorAgreements
parameter_list|()
block|{
return|return
name|useContributorAgreements
return|;
block|}
DECL|method|getUseSignedOffBy ()
specifier|public
name|InheritableBoolean
name|getUseSignedOffBy
parameter_list|()
block|{
return|return
name|useSignedOffBy
return|;
block|}
DECL|method|getUseContentMerge ()
specifier|public
name|InheritableBoolean
name|getUseContentMerge
parameter_list|()
block|{
return|return
name|useContentMerge
return|;
block|}
DECL|method|getRequireChangeID ()
specifier|public
name|InheritableBoolean
name|getRequireChangeID
parameter_list|()
block|{
return|return
name|requireChangeID
return|;
block|}
DECL|method|setUseContributorAgreements (final InheritableBoolean u)
specifier|public
name|void
name|setUseContributorAgreements
parameter_list|(
specifier|final
name|InheritableBoolean
name|u
parameter_list|)
block|{
name|useContributorAgreements
operator|=
name|u
expr_stmt|;
block|}
DECL|method|setUseSignedOffBy (final InheritableBoolean sbo)
specifier|public
name|void
name|setUseSignedOffBy
parameter_list|(
specifier|final
name|InheritableBoolean
name|sbo
parameter_list|)
block|{
name|useSignedOffBy
operator|=
name|sbo
expr_stmt|;
block|}
DECL|method|setUseContentMerge (final InheritableBoolean cm)
specifier|public
name|void
name|setUseContentMerge
parameter_list|(
specifier|final
name|InheritableBoolean
name|cm
parameter_list|)
block|{
name|useContentMerge
operator|=
name|cm
expr_stmt|;
block|}
DECL|method|setRequireChangeID (final InheritableBoolean cid)
specifier|public
name|void
name|setRequireChangeID
parameter_list|(
specifier|final
name|InheritableBoolean
name|cid
parameter_list|)
block|{
name|requireChangeID
operator|=
name|cid
expr_stmt|;
block|}
DECL|method|getSubmitType ()
specifier|public
name|SubmitType
name|getSubmitType
parameter_list|()
block|{
return|return
name|submitType
return|;
block|}
DECL|method|setSubmitType (final SubmitType type)
specifier|public
name|void
name|setSubmitType
parameter_list|(
specifier|final
name|SubmitType
name|type
parameter_list|)
block|{
name|submitType
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getState ()
specifier|public
name|State
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
DECL|method|setState (final State newState)
specifier|public
name|void
name|setState
parameter_list|(
specifier|final
name|State
name|newState
parameter_list|)
block|{
name|state
operator|=
name|newState
expr_stmt|;
block|}
DECL|method|getDefaultDashboard ()
specifier|public
name|String
name|getDefaultDashboard
parameter_list|()
block|{
return|return
name|defaultDashboardId
return|;
block|}
DECL|method|setDefaultDashboard (final String defaultDashboardId)
specifier|public
name|void
name|setDefaultDashboard
parameter_list|(
specifier|final
name|String
name|defaultDashboardId
parameter_list|)
block|{
name|this
operator|.
name|defaultDashboardId
operator|=
name|defaultDashboardId
expr_stmt|;
block|}
DECL|method|getLocalDefaultDashboard ()
specifier|public
name|String
name|getLocalDefaultDashboard
parameter_list|()
block|{
return|return
name|localDefaultDashboardId
return|;
block|}
DECL|method|setLocalDefaultDashboard (final String localDefaultDashboardId)
specifier|public
name|void
name|setLocalDefaultDashboard
parameter_list|(
specifier|final
name|String
name|localDefaultDashboardId
parameter_list|)
block|{
name|this
operator|.
name|localDefaultDashboardId
operator|=
name|localDefaultDashboardId
expr_stmt|;
block|}
DECL|method|getThemeName ()
specifier|public
name|String
name|getThemeName
parameter_list|()
block|{
return|return
name|themeName
return|;
block|}
DECL|method|setThemeName (final String themeName)
specifier|public
name|void
name|setThemeName
parameter_list|(
specifier|final
name|String
name|themeName
parameter_list|)
block|{
name|this
operator|.
name|themeName
operator|=
name|themeName
expr_stmt|;
block|}
DECL|method|copySettingsFrom (final Project update)
specifier|public
name|void
name|copySettingsFrom
parameter_list|(
specifier|final
name|Project
name|update
parameter_list|)
block|{
name|description
operator|=
name|update
operator|.
name|description
expr_stmt|;
name|useContributorAgreements
operator|=
name|update
operator|.
name|useContributorAgreements
expr_stmt|;
name|useSignedOffBy
operator|=
name|update
operator|.
name|useSignedOffBy
expr_stmt|;
name|useContentMerge
operator|=
name|update
operator|.
name|useContentMerge
expr_stmt|;
name|requireChangeID
operator|=
name|update
operator|.
name|requireChangeID
expr_stmt|;
name|submitType
operator|=
name|update
operator|.
name|submitType
expr_stmt|;
name|state
operator|=
name|update
operator|.
name|state
expr_stmt|;
block|}
comment|/**    * Returns the name key of the parent project.    *    * @return name key of the parent project,<code>null</code> if this project    *         is the wild project,<code>null</code> or the name key of the wild    *         project if this project is a direct child of the wild project    */
DECL|method|getParent ()
specifier|public
name|Project
operator|.
name|NameKey
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
comment|/**    * Returns the name key of the parent project.    *    * @param allProjectsName name key of the wild project    * @return name key of the parent project,<code>null</code> if this project    *         is the wild project    */
DECL|method|getParent (final Project.NameKey allProjectsName)
specifier|public
name|Project
operator|.
name|NameKey
name|getParent
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|allProjectsName
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
return|;
block|}
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|allProjectsName
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|allProjectsName
return|;
block|}
DECL|method|getParentName ()
specifier|public
name|String
name|getParentName
parameter_list|()
block|{
return|return
name|parent
operator|!=
literal|null
condition|?
name|parent
operator|.
name|get
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|setParentName (String n)
specifier|public
name|void
name|setParentName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|parent
operator|=
name|n
operator|!=
literal|null
condition|?
operator|new
name|NameKey
argument_list|(
name|n
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
DECL|method|setParentName (NameKey n)
specifier|public
name|void
name|setParentName
parameter_list|(
name|NameKey
name|n
parameter_list|)
block|{
name|parent
operator|=
name|n
expr_stmt|;
block|}
block|}
end_class

end_unit

