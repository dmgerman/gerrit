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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Scopes
operator|.
name|SINGLETON
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
name|client
operator|.
name|data
operator|.
name|GerritConfig
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|SystemConfig
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
name|git
operator|.
name|ChangeMergeQueue
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
name|git
operator|.
name|MergeQueue
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
name|git
operator|.
name|PushReplication
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
name|git
operator|.
name|ReplicationQueue
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
name|patch
operator|.
name|PatchSetInfoFactory
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
name|config
operator|.
name|GerritServerConfig
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
name|config
operator|.
name|GerritServerConfigProvider
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
name|config
operator|.
name|SitePath
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
name|config
operator|.
name|SitePathProvider
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
name|mail
operator|.
name|AbandonedSender
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
name|mail
operator|.
name|AddReviewerSender
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
name|mail
operator|.
name|CreateChangeSender
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
name|mail
operator|.
name|EmailSender
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
name|mail
operator|.
name|SmtpEmailSender
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
name|SchemaFactory
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
name|jdbc
operator|.
name|Database
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
name|Key
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
name|TypeLiteral
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
name|assistedinject
operator|.
name|FactoryProvider
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
name|name
operator|.
name|Names
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_comment
comment|/** Starts {@link GerritServer} with standard dependencies. */
end_comment

begin_class
DECL|class|GerritServerModule
specifier|public
class|class
name|GerritServerModule
extends|extends
name|AbstractModule
block|{
DECL|field|DS
specifier|public
specifier|static
specifier|final
name|Key
argument_list|<
name|DataSource
argument_list|>
name|DS
init|=
name|Key
operator|.
name|get
argument_list|(
name|DataSource
operator|.
name|class
argument_list|,
name|Names
operator|.
name|named
argument_list|(
literal|"ReviewDb"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|DS
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ReviewDbDataSourceProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
operator|new
name|TypeLiteral
argument_list|<
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|to
argument_list|(
operator|new
name|TypeLiteral
argument_list|<
name|Database
argument_list|<
name|ReviewDb
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
expr_stmt|;
name|bind
argument_list|(
operator|new
name|TypeLiteral
argument_list|<
name|Database
argument_list|<
name|ReviewDb
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ReviewDbProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SystemConfig
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SystemConfigProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|File
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|SitePath
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SitePathProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Config
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|GerritServerConfig
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|GerritServerConfigProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GerritServer
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ContactStore
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|EncryptedContactStoreProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|FileTypeRegistry
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MimeUtilFileTypeRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ReplicationQueue
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|PushReplication
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|MergeQueue
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ChangeMergeQueue
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|EmailSender
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SmtpEmailSender
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GerritConfig
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|GerritConfigProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|PatchSetInfoFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AddReviewerSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|FactoryProvider
operator|.
name|newFactory
argument_list|(
name|AddReviewerSender
operator|.
name|Factory
operator|.
name|class
argument_list|,
name|AddReviewerSender
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|CreateChangeSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|FactoryProvider
operator|.
name|newFactory
argument_list|(
name|CreateChangeSender
operator|.
name|Factory
operator|.
name|class
argument_list|,
name|CreateChangeSender
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AbandonedSender
operator|.
name|Factory
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|FactoryProvider
operator|.
name|newFactory
argument_list|(
name|AbandonedSender
operator|.
name|Factory
operator|.
name|class
argument_list|,
name|AbandonedSender
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

