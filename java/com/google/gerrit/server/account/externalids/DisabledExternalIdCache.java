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
DECL|package|com.google.gerrit.server.account.externalids
package|package
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
name|externalids
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
name|ImmutableSet
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
name|ImmutableSetMultimap
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
name|entities
operator|.
name|Account
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
name|AbstractModule
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
name|Module
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_class
DECL|class|DisabledExternalIdCache
specifier|public
class|class
name|DisabledExternalIdCache
implements|implements
name|ExternalIdCache
block|{
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|ExternalIdCache
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DisabledExternalIdCache
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|onReplace ( ObjectId oldNotesRev, ObjectId newNotesRev, Collection<ExternalId> toRemove, Collection<ExternalId> toAdd)
specifier|public
name|void
name|onReplace
parameter_list|(
name|ObjectId
name|oldNotesRev
parameter_list|,
name|ObjectId
name|newNotesRev
parameter_list|,
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|toRemove
parameter_list|,
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|toAdd
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|byAccount (Account.Id accountId)
specifier|public
name|ImmutableSet
argument_list|<
name|ExternalId
argument_list|>
name|byAccount
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|byAccount (Account.Id accountId, ObjectId rev)
specifier|public
name|ImmutableSet
argument_list|<
name|ExternalId
argument_list|>
name|byAccount
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|ObjectId
name|rev
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|allByAccount ()
specifier|public
name|ImmutableSetMultimap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ExternalId
argument_list|>
name|allByAccount
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|byEmails (String... emails)
specifier|public
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|ExternalId
argument_list|>
name|byEmails
parameter_list|(
name|String
modifier|...
name|emails
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|allByEmail ()
specifier|public
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|ExternalId
argument_list|>
name|allByEmail
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

