begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|account
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
name|Lists
import|;
end_import

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
name|Maps
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
name|common
operator|.
name|data
operator|.
name|AgreementInfo
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
name|common
operator|.
name|data
operator|.
name|ContributorAgreement
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
operator|.
name|Action
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
name|httpd
operator|.
name|rpc
operator|.
name|Handler
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
name|server
operator|.
name|IdentifiedUser
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
name|ProjectCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_class
DECL|class|AgreementInfoFactory
class|class
name|AgreementInfoFactory
extends|extends
name|Handler
argument_list|<
name|AgreementInfo
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ()
name|AgreementInfoFactory
name|create
parameter_list|()
function_decl|;
block|}
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|info
specifier|private
name|AgreementInfo
name|info
decl_stmt|;
annotation|@
name|Inject
DECL|method|AgreementInfoFactory (final IdentifiedUser user, final ProjectCache projectCache)
name|AgreementInfoFactory
parameter_list|(
specifier|final
name|IdentifiedUser
name|user
parameter_list|,
specifier|final
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|AgreementInfo
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|accepted
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|ContributorAgreement
argument_list|>
name|agreements
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ContributorAgreement
argument_list|>
name|cas
init|=
name|projectCache
operator|.
name|getAllProjects
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getContributorAgreements
argument_list|()
decl_stmt|;
for|for
control|(
name|ContributorAgreement
name|ca
range|:
name|cas
control|)
block|{
name|agreements
operator|.
name|put
argument_list|(
name|ca
operator|.
name|getName
argument_list|()
argument_list|,
name|ca
operator|.
name|forUi
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|PermissionRule
name|rule
range|:
name|ca
operator|.
name|getAccepted
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
name|rule
operator|.
name|getAction
argument_list|()
operator|==
name|Action
operator|.
name|ALLOW
operator|)
operator|&&
operator|(
name|rule
operator|.
name|getGroup
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
if|if
condition|(
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"group \""
operator|+
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"\" does not "
operator|+
literal|" exist, referenced in CLA \""
operator|+
name|ca
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|groupIds
operator|.
name|add
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|user
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|containsAnyOf
argument_list|(
name|groupIds
argument_list|)
condition|)
block|{
name|accepted
operator|.
name|add
argument_list|(
name|ca
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|info
operator|=
operator|new
name|AgreementInfo
argument_list|()
expr_stmt|;
name|info
operator|.
name|setAccepted
argument_list|(
name|accepted
argument_list|)
expr_stmt|;
name|info
operator|.
name|setAgreements
argument_list|(
name|agreements
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
block|}
end_class

end_unit

