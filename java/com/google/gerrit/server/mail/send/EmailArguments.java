begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail.send
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
operator|.
name|send
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|ApprovalsUtil
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
name|GerritPersonIdentProvider
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
name|config
operator|.
name|SitePaths
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
name|mail
operator|.
name|EmailSettings
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
name|notedb
operator|.
name|ChangeNotes
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
name|permissions
operator|.
name|PermissionBackend
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
name|account
operator|.
name|InternalAccountQuery
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
name|ChangeData
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
name|gerrit
operator|.
name|server
operator|.
name|validators
operator|.
name|OutgoingEmailValidationListener
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
name|template
operator|.
name|soy
operator|.
name|tofu
operator|.
name|SoyTofu
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
import|;
end_import

begin_class
DECL|class|EmailArguments
specifier|public
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
DECL|field|permissionBackend
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|groupBackend
specifier|final
name|GroupBackend
name|groupBackend
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
DECL|field|approvalsUtil
specifier|final
name|ApprovalsUtil
name|approvalsUtil
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
DECL|field|changeNotesFactory
specifier|final
name|ChangeNotes
operator|.
name|Factory
name|changeNotesFactory
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
DECL|field|gerritPersonIdent
specifier|final
name|PersonIdent
name|gerritPersonIdent
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
DECL|field|site
specifier|final
name|SitePaths
name|site
decl_stmt|;
DECL|field|queryBuilder
specifier|final
name|ChangeQueryBuilder
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
DECL|field|changeDataFactory
specifier|final
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
decl_stmt|;
DECL|field|soyTofu
specifier|final
name|SoyTofu
name|soyTofu
decl_stmt|;
DECL|field|settings
specifier|final
name|EmailSettings
name|settings
decl_stmt|;
DECL|field|outgoingEmailValidationListeners
specifier|final
name|DynamicSet
argument_list|<
name|OutgoingEmailValidationListener
argument_list|>
name|outgoingEmailValidationListeners
decl_stmt|;
DECL|field|accountQueryProvider
specifier|final
name|Provider
argument_list|<
name|InternalAccountQuery
argument_list|>
name|accountQueryProvider
decl_stmt|;
DECL|field|validator
specifier|final
name|OutgoingEmailValidator
name|validator
decl_stmt|;
annotation|@
name|Inject
DECL|method|EmailArguments ( GitRepositoryManager server, ProjectCache projectCache, PermissionBackend permissionBackend, GroupBackend groupBackend, AccountCache accountCache, PatchListCache patchListCache, ApprovalsUtil approvalsUtil, FromAddressGenerator fromAddressGenerator, EmailSender emailSender, PatchSetInfoFactory patchSetInfoFactory, GenericFactory identifiedUserFactory, ChangeNotes.Factory changeNotesFactory, AnonymousUser anonymousUser, @AnonymousCowardName String anonymousCowardName, GerritPersonIdentProvider gerritPersonIdentProvider, @CanonicalWebUrl @Nullable Provider<String> urlProvider, AllProjectsName allProjectsName, ChangeQueryBuilder queryBuilder, Provider<ReviewDb> db, ChangeData.Factory changeDataFactory, @MailTemplates SoyTofu soyTofu, EmailSettings settings, @SshAdvertisedAddresses List<String> sshAddresses, SitePaths site, DynamicSet<OutgoingEmailValidationListener> outgoingEmailValidationListeners, Provider<InternalAccountQuery> accountQueryProvider, OutgoingEmailValidator validator)
name|EmailArguments
parameter_list|(
name|GitRepositoryManager
name|server
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|PatchListCache
name|patchListCache
parameter_list|,
name|ApprovalsUtil
name|approvalsUtil
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
name|ChangeNotes
operator|.
name|Factory
name|changeNotesFactory
parameter_list|,
name|AnonymousUser
name|anonymousUser
parameter_list|,
annotation|@
name|AnonymousCowardName
name|String
name|anonymousCowardName
parameter_list|,
name|GerritPersonIdentProvider
name|gerritPersonIdentProvider
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
name|queryBuilder
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
annotation|@
name|MailTemplates
name|SoyTofu
name|soyTofu
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
parameter_list|,
name|SitePaths
name|site
parameter_list|,
name|DynamicSet
argument_list|<
name|OutgoingEmailValidationListener
argument_list|>
name|outgoingEmailValidationListeners
parameter_list|,
name|Provider
argument_list|<
name|InternalAccountQuery
argument_list|>
name|accountQueryProvider
parameter_list|,
name|OutgoingEmailValidator
name|validator
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
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
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
name|approvalsUtil
operator|=
name|approvalsUtil
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
name|changeNotesFactory
operator|=
name|changeNotesFactory
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
name|gerritPersonIdent
operator|=
name|gerritPersonIdentProvider
operator|.
name|get
argument_list|()
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
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
name|this
operator|.
name|soyTofu
operator|=
name|soyTofu
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
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
name|this
operator|.
name|outgoingEmailValidationListeners
operator|=
name|outgoingEmailValidationListeners
expr_stmt|;
name|this
operator|.
name|accountQueryProvider
operator|=
name|accountQueryProvider
expr_stmt|;
name|this
operator|.
name|validator
operator|=
name|validator
expr_stmt|;
block|}
block|}
end_class

end_unit
