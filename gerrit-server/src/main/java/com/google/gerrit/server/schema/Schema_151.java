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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|Streams
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
name|client
operator|.
name|AccountGroupMemberAudit
operator|.
name|Key
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
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
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
name|java
operator|.
name|sql
operator|.
name|Timestamp
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/** A schema which adds the 'created on' field to groups. */
end_comment

begin_class
DECL|class|Schema_151
specifier|public
class|class
name|Schema_151
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_151 (Provider<Schema_150> prior)
specifier|protected
name|Schema_151
parameter_list|(
name|Provider
argument_list|<
name|Schema_150
argument_list|>
name|prior
parameter_list|)
block|{
name|super
argument_list|(
name|prior
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|migrateData (ReviewDb db, UpdateUI ui)
specifier|protected
name|void
name|migrateData
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|OrmException
block|{
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|accountGroups
init|=
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|toList
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroup
name|accountGroup
range|:
name|accountGroups
control|)
block|{
name|ResultSet
argument_list|<
name|AccountGroupMemberAudit
argument_list|>
name|groupMemberAudits
init|=
name|db
operator|.
name|accountGroupMembersAudit
argument_list|()
operator|.
name|byGroup
argument_list|(
name|accountGroup
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|Timestamp
argument_list|>
name|firstTimeMentioned
init|=
name|Streams
operator|.
name|stream
argument_list|(
name|groupMemberAudits
argument_list|)
operator|.
name|map
argument_list|(
name|AccountGroupMemberAudit
operator|::
name|getKey
argument_list|)
operator|.
name|map
argument_list|(
name|Key
operator|::
name|getAddedOn
argument_list|)
operator|.
name|min
argument_list|(
name|Comparator
operator|.
name|naturalOrder
argument_list|()
argument_list|)
decl_stmt|;
name|Timestamp
name|createdOn
init|=
name|firstTimeMentioned
operator|.
name|orElseGet
argument_list|(
parameter_list|()
lambda|->
name|AccountGroup
operator|.
name|auditCreationInstantTs
argument_list|()
argument_list|)
decl_stmt|;
name|accountGroup
operator|.
name|setCreatedOn
argument_list|(
name|createdOn
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|update
argument_list|(
name|accountGroups
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

