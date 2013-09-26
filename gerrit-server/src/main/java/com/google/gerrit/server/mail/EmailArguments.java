begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
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
name|Nullable
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
name|AnonymousUser
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
name|IdentifiedUser
operator|.
name|GenericFactory
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
name|AccountCache
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
name|CapabilityControl
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
name|GroupIncludeCache
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
name|AllProjectsName
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
name|AnonymousCowardName
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
name|CanonicalWebUrl
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
name|git
operator|.
name|GitRepositoryManager
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
name|PatchListCache
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
name|project
operator|.
name|ProjectCache
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
name|query
operator|.
name|change
operator|.
name|ChangeQueryBuilder
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
name|ssh
operator|.
name|SshAdvertisedAddresses
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
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|RuntimeInstance
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
DECL|class|EmailArguments
class|class
name|EmailArguments
block|{
DECL|field|server
specifier|final
name|GitRepositoryManager
name|server
decl_stmt|;
DECL|field|projectCache
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|groupBackend
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|groupIncludes
specifier|final
name|GroupIncludeCache
name|groupIncludes
decl_stmt|;
DECL|field|accountCache
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|patchListCache
specifier|final
name|PatchListCache
name|patchListCache
decl_stmt|;
DECL|field|fromAddressGenerator
specifier|final
name|FromAddressGenerator
name|fromAddressGenerator
decl_stmt|;
DECL|field|emailSender
specifier|final
name|EmailSender
name|emailSender
decl_stmt|;
DECL|field|patchSetInfoFactory
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|identifiedUserFactory
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
DECL|field|capabilityControlFactory
specifier|final
name|CapabilityControl
operator|.
name|Factory
name|capabilityControlFactory
decl_stmt|;
DECL|field|anonymousUser
specifier|final
name|AnonymousUser
name|anonymousUser
decl_stmt|;
DECL|field|anonymousCowardName
specifier|final
name|String
name|anonymousCowardName
decl_stmt|;
DECL|field|urlProvider
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
decl_stmt|;
DECL|field|allProjectsName
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|sshAddresses
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|sshAddresses
decl_stmt|;
DECL|field|queryBuilder
specifier|final
name|ChangeQueryBuilder
operator|.
name|Factory
name|queryBuilder
decl_stmt|;
DECL|field|db
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|velocityRuntime
specifier|final
name|RuntimeInstance
name|velocityRuntime
decl_stmt|;
DECL|field|settings
specifier|final
name|EmailSettings
name|settings
decl_stmt|;
annotation|@
name|Inject
DECL|method|EmailArguments (GitRepositoryManager server, ProjectCache projectCache, GroupBackend groupBackend, GroupIncludeCache groupIncludes, AccountCache accountCache, PatchListCache patchListCache, FromAddressGenerator fromAddressGenerator, EmailSender emailSender, PatchSetInfoFactory patchSetInfoFactory, GenericFactory identifiedUserFactory, CapabilityControl.Factory capabilityControlFactory, AnonymousUser anonymousUser, @AnonymousCowardName String anonymousCowardName, @CanonicalWebUrl @Nullable Provider<String> urlProvider, AllProjectsName allProjectsName, ChangeQueryBuilder.Factory queryBuilder, Provider<ReviewDb> db, RuntimeInstance velocityRuntime, EmailSettings settings, @SshAdvertisedAddresses List<String> sshAddresses)
name|EmailArguments
parameter_list|(
name|GitRepositoryManager
name|server
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|GroupIncludeCache
name|groupIncludes
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|PatchListCache
name|patchListCache
parameter_list|,
name|FromAddressGenerator
name|fromAddressGenerator
parameter_list|,
name|EmailSender
name|emailSender
parameter_list|,
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
name|GenericFactory
name|identifiedUserFactory
parameter_list|,
name|CapabilityControl
operator|.
name|Factory
name|capabilityControlFactory
parameter_list|,
name|AnonymousUser
name|anonymousUser
parameter_list|,
annotation|@
name|AnonymousCowardName
name|String
name|anonymousCowardName
parameter_list|,
annotation|@
name|CanonicalWebUrl
annotation|@
name|Nullable
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
parameter_list|,
name|AllProjectsName
name|allProjectsName
parameter_list|,
name|ChangeQueryBuilder
operator|.
name|Factory
name|queryBuilder
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|RuntimeInstance
name|velocityRuntime
parameter_list|,
name|EmailSettings
name|settings
parameter_list|,
annotation|@
name|SshAdvertisedAddresses
name|List
argument_list|<
name|String
argument_list|>
name|sshAddresses
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|groupIncludes
operator|=
name|groupIncludes
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|patchListCache
operator|=
name|patchListCache
expr_stmt|;
name|this
operator|.
name|fromAddressGenerator
operator|=
name|fromAddressGenerator
expr_stmt|;
name|this
operator|.
name|emailSender
operator|=
name|emailSender
expr_stmt|;
name|this
operator|.
name|patchSetInfoFactory
operator|=
name|patchSetInfoFactory
expr_stmt|;
name|this
operator|.
name|identifiedUserFactory
operator|=
name|identifiedUserFactory
expr_stmt|;
name|this
operator|.
name|capabilityControlFactory
operator|=
name|capabilityControlFactory
expr_stmt|;
name|this
operator|.
name|anonymousUser
operator|=
name|anonymousUser
expr_stmt|;
name|this
operator|.
name|anonymousCowardName
operator|=
name|anonymousCowardName
expr_stmt|;
name|this
operator|.
name|urlProvider
operator|=
name|urlProvider
expr_stmt|;
name|this
operator|.
name|allProjectsName
operator|=
name|allProjectsName
expr_stmt|;
name|this
operator|.
name|queryBuilder
operator|=
name|queryBuilder
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|velocityRuntime
operator|=
name|velocityRuntime
expr_stmt|;
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
name|this
operator|.
name|sshAddresses
operator|=
name|sshAddresses
expr_stmt|;
block|}
block|}
end_class

end_unit

