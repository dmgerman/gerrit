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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
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
name|GroupDescription
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
name|GroupDescriptions
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
name|restapi
operator|.
name|AuthException
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
name|MethodNotAllowedException
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
name|RestReadView
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
name|Url
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
name|AccountGroupByIdAud
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
name|AccountGroupMemberAudit
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
name|server
operator|.
name|ReviewDb
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
name|account
operator|.
name|AccountLoader
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
name|account
operator|.
name|GroupBackend
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
name|account
operator|.
name|GroupCache
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
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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

begin_class
annotation|@
name|Singleton
DECL|class|GetAuditLog
specifier|public
class|class
name|GetAuditLog
implements|implements
name|RestReadView
argument_list|<
name|GroupResource
argument_list|>
block|{
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|accountLoaderFactory
specifier|private
specifier|final
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|groupJson
specifier|private
specifier|final
name|GroupJson
name|groupJson
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetAuditLog ( Provider<ReviewDb> db, AccountLoader.Factory accountLoaderFactory, GroupCache groupCache, GroupJson groupJson, GroupBackend groupBackend)
specifier|public
name|GetAuditLog
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
parameter_list|,
name|GroupCache
name|groupCache
parameter_list|,
name|GroupJson
name|groupJson
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|accountLoaderFactory
operator|=
name|accountLoaderFactory
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|groupJson
operator|=
name|groupJson
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource rsrc)
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|GroupAuditEventInfo
argument_list|>
name|apply
parameter_list|(
name|GroupResource
name|rsrc
parameter_list|)
throws|throws
name|AuthException
throws|,
name|MethodNotAllowedException
throws|,
name|OrmException
block|{
name|GroupDescription
operator|.
name|Internal
name|group
init|=
name|rsrc
operator|.
name|asInternalGroup
argument_list|()
operator|.
name|orElseThrow
argument_list|(
name|MethodNotAllowedException
operator|::
operator|new
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|isOwner
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Not group owner"
argument_list|)
throw|;
block|}
name|AccountLoader
name|accountLoader
init|=
name|accountLoaderFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|GroupAuditEventInfo
argument_list|>
name|auditEvents
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroupMemberAudit
name|auditEvent
range|:
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountGroupMembersAudit
argument_list|()
operator|.
name|byGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
control|)
block|{
name|AccountInfo
name|member
init|=
name|accountLoader
operator|.
name|get
argument_list|(
name|auditEvent
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
name|auditEvents
operator|.
name|add
argument_list|(
name|GroupAuditEventInfo
operator|.
name|createAddUserEvent
argument_list|(
name|accountLoader
operator|.
name|get
argument_list|(
name|auditEvent
operator|.
name|getAddedBy
argument_list|()
argument_list|)
argument_list|,
name|auditEvent
operator|.
name|getKey
argument_list|()
operator|.
name|getAddedOn
argument_list|()
argument_list|,
name|member
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|auditEvent
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|auditEvents
operator|.
name|add
argument_list|(
name|GroupAuditEventInfo
operator|.
name|createRemoveUserEvent
argument_list|(
name|accountLoader
operator|.
name|get
argument_list|(
name|auditEvent
operator|.
name|getRemovedBy
argument_list|()
argument_list|)
argument_list|,
name|auditEvent
operator|.
name|getRemovedOn
argument_list|()
argument_list|,
name|member
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|AccountGroupByIdAud
name|auditEvent
range|:
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountGroupByIdAud
argument_list|()
operator|.
name|byGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
control|)
block|{
name|AccountGroup
operator|.
name|UUID
name|includedGroupUUID
init|=
name|auditEvent
operator|.
name|getKey
argument_list|()
operator|.
name|getIncludeUUID
argument_list|()
decl_stmt|;
name|AccountGroup
name|includedGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|includedGroupUUID
argument_list|)
decl_stmt|;
name|GroupInfo
name|member
decl_stmt|;
if|if
condition|(
name|includedGroup
operator|!=
literal|null
condition|)
block|{
name|member
operator|=
name|groupJson
operator|.
name|format
argument_list|(
name|GroupDescriptions
operator|.
name|forAccountGroup
argument_list|(
name|includedGroup
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|GroupDescription
operator|.
name|Basic
name|groupDescription
init|=
name|groupBackend
operator|.
name|get
argument_list|(
name|includedGroupUUID
argument_list|)
decl_stmt|;
name|member
operator|=
operator|new
name|GroupInfo
argument_list|()
expr_stmt|;
name|member
operator|.
name|id
operator|=
name|Url
operator|.
name|encode
argument_list|(
name|includedGroupUUID
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|member
operator|.
name|name
operator|=
name|groupDescription
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|auditEvents
operator|.
name|add
argument_list|(
name|GroupAuditEventInfo
operator|.
name|createAddGroupEvent
argument_list|(
name|accountLoader
operator|.
name|get
argument_list|(
name|auditEvent
operator|.
name|getAddedBy
argument_list|()
argument_list|)
argument_list|,
name|auditEvent
operator|.
name|getKey
argument_list|()
operator|.
name|getAddedOn
argument_list|()
argument_list|,
name|member
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|auditEvent
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|auditEvents
operator|.
name|add
argument_list|(
name|GroupAuditEventInfo
operator|.
name|createRemoveGroupEvent
argument_list|(
name|accountLoader
operator|.
name|get
argument_list|(
name|auditEvent
operator|.
name|getRemovedBy
argument_list|()
argument_list|)
argument_list|,
name|auditEvent
operator|.
name|getRemovedOn
argument_list|()
argument_list|,
name|member
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|accountLoader
operator|.
name|fill
argument_list|()
expr_stmt|;
comment|// sort by date in reverse order so that the newest audit event comes first
name|Collections
operator|.
name|sort
argument_list|(
name|auditEvents
argument_list|,
operator|new
name|Comparator
argument_list|<
name|GroupAuditEventInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|GroupAuditEventInfo
name|e1
parameter_list|,
name|GroupAuditEventInfo
name|e2
parameter_list|)
block|{
return|return
name|e2
operator|.
name|date
operator|.
name|compareTo
argument_list|(
name|e1
operator|.
name|date
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|auditEvents
return|;
block|}
block|}
end_class

end_unit

