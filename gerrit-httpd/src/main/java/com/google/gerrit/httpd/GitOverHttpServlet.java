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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|cache
operator|.
name|Cache
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
name|Lists
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
name|Capable
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
name|AccessPath
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
name|CurrentUser
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
name|cache
operator|.
name|CacheModule
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
name|AsyncReceiveCommits
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
name|ChangeCache
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
name|git
operator|.
name|ReceiveCommits
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
name|ReceivePackInitializer
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
name|TagCache
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
name|TransferConfig
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
name|VisibleRefFilter
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
name|validators
operator|.
name|UploadValidators
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
name|NoSuchProjectException
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
name|ProjectControl
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
name|name
operator|.
name|Named
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|http
operator|.
name|server
operator|.
name|GitServlet
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
name|http
operator|.
name|server
operator|.
name|GitSmartHttpTools
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
name|http
operator|.
name|server
operator|.
name|ServletUtils
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
name|http
operator|.
name|server
operator|.
name|resolver
operator|.
name|AsIsFileService
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
name|Repository
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
name|transport
operator|.
name|PostReceiveHook
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
name|transport
operator|.
name|PostReceiveHookChain
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
name|transport
operator|.
name|PreUploadHook
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
name|transport
operator|.
name|PreUploadHookChain
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
name|transport
operator|.
name|ReceivePack
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
name|transport
operator|.
name|UploadPack
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
name|transport
operator|.
name|resolver
operator|.
name|ReceivePackFactory
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
name|transport
operator|.
name|resolver
operator|.
name|RepositoryResolver
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
name|transport
operator|.
name|resolver
operator|.
name|ServiceNotAuthorizedException
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
name|transport
operator|.
name|resolver
operator|.
name|ServiceNotEnabledException
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
name|transport
operator|.
name|resolver
operator|.
name|UploadPackFactory
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_comment
comment|/** Serves Git repositories over HTTP. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GitOverHttpServlet
specifier|public
class|class
name|GitOverHttpServlet
extends|extends
name|GitServlet
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
DECL|field|ATT_CONTROL
specifier|private
specifier|static
specifier|final
name|String
name|ATT_CONTROL
init|=
name|ProjectControl
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|ATT_RC
specifier|private
specifier|static
specifier|final
name|String
name|ATT_RC
init|=
name|ReceiveCommits
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|ID_CACHE
specifier|private
specifier|static
specifier|final
name|String
name|ID_CACHE
init|=
literal|"adv_bases"
decl_stmt|;
DECL|field|URL_REGEX
specifier|public
specifier|static
specifier|final
name|String
name|URL_REGEX
decl_stmt|;
static|static
block|{
name|StringBuilder
name|url
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|url
operator|.
name|append
argument_list|(
literal|"^(?:/p/|/)(.*/(?:info/refs"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|GitSmartHttpTools
operator|.
name|VALID_SERVICES
control|)
block|{
name|url
operator|.
name|append
argument_list|(
literal|'|'
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|url
operator|.
name|append
argument_list|(
literal|"))$"
argument_list|)
expr_stmt|;
name|URL_REGEX
operator|=
name|url
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
DECL|class|Module
specifier|static
class|class
name|Module
extends|extends
name|AbstractModule
block|{
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
name|Resolver
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|UploadFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|UploadFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ReceiveFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ReceiveFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|cache
argument_list|(
name|ID_CACHE
argument_list|,
name|AdvertisedObjectsCacheKey
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|Set
argument_list|<
name|ObjectId
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|maximumWeight
argument_list|(
literal|4096
argument_list|)
operator|.
name|expireAfterWrite
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Inject
DECL|method|GitOverHttpServlet (Resolver resolver, UploadFactory upload, UploadFilter uploadFilter, ReceiveFactory receive, ReceiveFilter receiveFilter)
name|GitOverHttpServlet
parameter_list|(
name|Resolver
name|resolver
parameter_list|,
name|UploadFactory
name|upload
parameter_list|,
name|UploadFilter
name|uploadFilter
parameter_list|,
name|ReceiveFactory
name|receive
parameter_list|,
name|ReceiveFilter
name|receiveFilter
parameter_list|)
block|{
name|setRepositoryResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|setAsIsFileService
argument_list|(
name|AsIsFileService
operator|.
name|DISABLED
argument_list|)
expr_stmt|;
name|setUploadPackFactory
argument_list|(
name|upload
argument_list|)
expr_stmt|;
name|addUploadPackFilter
argument_list|(
name|uploadFilter
argument_list|)
expr_stmt|;
name|setReceivePackFactory
argument_list|(
name|receive
argument_list|)
expr_stmt|;
name|addReceivePackFilter
argument_list|(
name|receiveFilter
argument_list|)
expr_stmt|;
block|}
DECL|class|Resolver
specifier|static
class|class
name|Resolver
implements|implements
name|RepositoryResolver
argument_list|<
name|HttpServletRequest
argument_list|>
block|{
DECL|field|manager
specifier|private
specifier|final
name|GitRepositoryManager
name|manager
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|Resolver (GitRepositoryManager manager, ProjectControl.Factory projectControlFactory)
name|Resolver
parameter_list|(
name|GitRepositoryManager
name|manager
parameter_list|,
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|open (HttpServletRequest req, String projectName)
specifier|public
name|Repository
name|open
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|String
name|projectName
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|ServiceNotAuthorizedException
throws|,
name|ServiceNotEnabledException
block|{
while|while
condition|(
name|projectName
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|projectName
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectName
operator|.
name|endsWith
argument_list|(
literal|".git"
argument_list|)
condition|)
block|{
comment|// Be nice and drop the trailing ".git" suffix, which we never keep
comment|// in our database, but clients might mistakenly provide anyway.
comment|//
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|projectName
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
while|while
condition|(
name|projectName
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|projectName
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|ProjectControl
name|pc
decl_stmt|;
try|try
block|{
name|pc
operator|=
name|projectControlFactory
operator|.
name|controlFor
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|projectName
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryNotFoundException
argument_list|(
name|projectName
argument_list|)
throw|;
block|}
name|CurrentUser
name|user
init|=
name|pc
operator|.
name|getCurrentUser
argument_list|()
decl_stmt|;
name|user
operator|.
name|setAccessPath
argument_list|(
name|AccessPath
operator|.
name|GIT
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|pc
operator|.
name|isVisible
argument_list|()
condition|)
block|{
if|if
condition|(
name|user
operator|instanceof
name|AnonymousUser
condition|)
block|{
throw|throw
operator|new
name|ServiceNotAuthorizedException
argument_list|()
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|ServiceNotEnabledException
argument_list|()
throw|;
block|}
block|}
name|req
operator|.
name|setAttribute
argument_list|(
name|ATT_CONTROL
argument_list|,
name|pc
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|manager
operator|.
name|openRepository
argument_list|(
name|pc
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryNotFoundException
argument_list|(
name|pc
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|class|UploadFactory
specifier|static
class|class
name|UploadFactory
implements|implements
name|UploadPackFactory
argument_list|<
name|HttpServletRequest
argument_list|>
block|{
DECL|field|config
specifier|private
specifier|final
name|TransferConfig
name|config
decl_stmt|;
DECL|field|preUploadHooks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|PreUploadHook
argument_list|>
name|preUploadHooks
decl_stmt|;
annotation|@
name|Inject
DECL|method|UploadFactory (TransferConfig tc, DynamicSet<PreUploadHook> preUploadHooks)
name|UploadFactory
parameter_list|(
name|TransferConfig
name|tc
parameter_list|,
name|DynamicSet
argument_list|<
name|PreUploadHook
argument_list|>
name|preUploadHooks
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|tc
expr_stmt|;
name|this
operator|.
name|preUploadHooks
operator|=
name|preUploadHooks
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (HttpServletRequest req, Repository repo)
specifier|public
name|UploadPack
name|create
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|Repository
name|repo
parameter_list|)
block|{
name|UploadPack
name|up
init|=
operator|new
name|UploadPack
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|up
operator|.
name|setPackConfig
argument_list|(
name|config
operator|.
name|getPackConfig
argument_list|()
argument_list|)
expr_stmt|;
name|up
operator|.
name|setTimeout
argument_list|(
name|config
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|up
operator|.
name|setPreUploadHook
argument_list|(
name|PreUploadHookChain
operator|.
name|newChain
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|preUploadHooks
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|up
return|;
block|}
block|}
DECL|class|UploadFilter
specifier|static
class|class
name|UploadFilter
implements|implements
name|Filter
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
DECL|field|tagCache
specifier|private
specifier|final
name|TagCache
name|tagCache
decl_stmt|;
DECL|field|changeCache
specifier|private
specifier|final
name|ChangeCache
name|changeCache
decl_stmt|;
DECL|field|uploadValidatorsFactory
specifier|private
specifier|final
name|UploadValidators
operator|.
name|Factory
name|uploadValidatorsFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|UploadFilter (Provider<ReviewDb> db, TagCache tagCache, ChangeCache changeCache, UploadValidators.Factory uploadValidatorsFactory)
name|UploadFilter
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|TagCache
name|tagCache
parameter_list|,
name|ChangeCache
name|changeCache
parameter_list|,
name|UploadValidators
operator|.
name|Factory
name|uploadValidatorsFactory
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
name|tagCache
operator|=
name|tagCache
expr_stmt|;
name|this
operator|.
name|changeCache
operator|=
name|changeCache
expr_stmt|;
name|this
operator|.
name|uploadValidatorsFactory
operator|=
name|uploadValidatorsFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse response, FilterChain next)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|next
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
comment|// The Resolver above already checked READ access for us.
name|Repository
name|repo
init|=
name|ServletUtils
operator|.
name|getRepository
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|ProjectControl
name|pc
init|=
operator|(
name|ProjectControl
operator|)
name|request
operator|.
name|getAttribute
argument_list|(
name|ATT_CONTROL
argument_list|)
decl_stmt|;
name|UploadPack
name|up
init|=
operator|(
name|UploadPack
operator|)
name|request
operator|.
name|getAttribute
argument_list|(
name|ServletUtils
operator|.
name|ATTRIBUTE_HANDLER
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|pc
operator|.
name|canRunUploadPack
argument_list|()
condition|)
block|{
name|GitSmartHttpTools
operator|.
name|sendError
argument_list|(
operator|(
name|HttpServletRequest
operator|)
name|request
argument_list|,
operator|(
name|HttpServletResponse
operator|)
name|response
argument_list|,
name|HttpServletResponse
operator|.
name|SC_FORBIDDEN
argument_list|,
literal|"upload-pack not permitted on this server"
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// We use getRemoteHost() here instead of getRemoteAddr() because REMOTE_ADDR
comment|// may have been overridden by a proxy server -- we'll try to avoid this.
name|UploadValidators
name|uploadValidators
init|=
name|uploadValidatorsFactory
operator|.
name|create
argument_list|(
name|pc
operator|.
name|getProject
argument_list|()
argument_list|,
name|repo
argument_list|,
name|request
operator|.
name|getRemoteHost
argument_list|()
argument_list|)
decl_stmt|;
name|up
operator|.
name|setPreUploadHook
argument_list|(
name|PreUploadHookChain
operator|.
name|newChain
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|up
operator|.
name|getPreUploadHook
argument_list|()
argument_list|,
name|uploadValidators
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|pc
operator|.
name|allRefsAreVisible
argument_list|()
condition|)
block|{
name|up
operator|.
name|setAdvertiseRefsHook
argument_list|(
operator|new
name|VisibleRefFilter
argument_list|(
name|tagCache
argument_list|,
name|changeCache
argument_list|,
name|repo
argument_list|,
name|pc
argument_list|,
name|db
operator|.
name|get
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|next
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig config)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|config
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{     }
block|}
DECL|class|ReceiveFactory
specifier|static
class|class
name|ReceiveFactory
implements|implements
name|ReceivePackFactory
argument_list|<
name|HttpServletRequest
argument_list|>
block|{
DECL|field|factory
specifier|private
specifier|final
name|AsyncReceiveCommits
operator|.
name|Factory
name|factory
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|TransferConfig
name|config
decl_stmt|;
DECL|field|receivePackInitializers
specifier|private
name|DynamicSet
argument_list|<
name|ReceivePackInitializer
argument_list|>
name|receivePackInitializers
decl_stmt|;
DECL|field|postReceiveHooks
specifier|private
name|DynamicSet
argument_list|<
name|PostReceiveHook
argument_list|>
name|postReceiveHooks
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReceiveFactory (AsyncReceiveCommits.Factory factory, TransferConfig config, DynamicSet<ReceivePackInitializer> receivePackInitializers, DynamicSet<PostReceiveHook> postReceiveHooks)
name|ReceiveFactory
parameter_list|(
name|AsyncReceiveCommits
operator|.
name|Factory
name|factory
parameter_list|,
name|TransferConfig
name|config
parameter_list|,
name|DynamicSet
argument_list|<
name|ReceivePackInitializer
argument_list|>
name|receivePackInitializers
parameter_list|,
name|DynamicSet
argument_list|<
name|PostReceiveHook
argument_list|>
name|postReceiveHooks
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|receivePackInitializers
operator|=
name|receivePackInitializers
expr_stmt|;
name|this
operator|.
name|postReceiveHooks
operator|=
name|postReceiveHooks
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (HttpServletRequest req, Repository db)
specifier|public
name|ReceivePack
name|create
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|Repository
name|db
parameter_list|)
throws|throws
name|ServiceNotAuthorizedException
block|{
specifier|final
name|ProjectControl
name|pc
init|=
operator|(
name|ProjectControl
operator|)
name|req
operator|.
name|getAttribute
argument_list|(
name|ATT_CONTROL
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|pc
operator|.
name|getCurrentUser
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
operator|)
condition|)
block|{
comment|// Anonymous users are not permitted to push.
throw|throw
operator|new
name|ServiceNotAuthorizedException
argument_list|()
throw|;
block|}
specifier|final
name|IdentifiedUser
name|user
init|=
operator|(
name|IdentifiedUser
operator|)
name|pc
operator|.
name|getCurrentUser
argument_list|()
decl_stmt|;
specifier|final
name|ReceiveCommits
name|rc
init|=
name|factory
operator|.
name|create
argument_list|(
name|pc
argument_list|,
name|db
argument_list|)
operator|.
name|getReceiveCommits
argument_list|()
decl_stmt|;
name|ReceivePack
name|rp
init|=
name|rc
operator|.
name|getReceivePack
argument_list|()
decl_stmt|;
name|rp
operator|.
name|setRefLogIdent
argument_list|(
name|user
operator|.
name|newRefLogIdent
argument_list|()
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setTimeout
argument_list|(
name|config
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setMaxObjectSizeLimit
argument_list|(
name|config
operator|.
name|getMaxObjectSizeLimit
argument_list|()
argument_list|)
expr_stmt|;
name|init
argument_list|(
name|pc
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|rp
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setPostReceiveHook
argument_list|(
name|PostReceiveHookChain
operator|.
name|newChain
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|postReceiveHooks
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|req
operator|.
name|setAttribute
argument_list|(
name|ATT_RC
argument_list|,
name|rc
argument_list|)
expr_stmt|;
return|return
name|rp
return|;
block|}
DECL|method|init (Project.NameKey project, ReceivePack rp)
specifier|private
name|void
name|init
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ReceivePack
name|rp
parameter_list|)
block|{
for|for
control|(
name|ReceivePackInitializer
name|initializer
range|:
name|receivePackInitializers
control|)
block|{
name|initializer
operator|.
name|init
argument_list|(
name|project
argument_list|,
name|rp
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|ReceiveFilter
specifier|static
class|class
name|ReceiveFilter
implements|implements
name|Filter
block|{
DECL|field|cache
specifier|private
specifier|final
name|Cache
argument_list|<
name|AdvertisedObjectsCacheKey
argument_list|,
name|Set
argument_list|<
name|ObjectId
argument_list|>
argument_list|>
name|cache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReceiveFilter ( @amedID_CACHE) Cache<AdvertisedObjectsCacheKey, Set<ObjectId>> cache)
name|ReceiveFilter
parameter_list|(
annotation|@
name|Named
argument_list|(
name|ID_CACHE
argument_list|)
name|Cache
argument_list|<
name|AdvertisedObjectsCacheKey
argument_list|,
name|Set
argument_list|<
name|ObjectId
argument_list|>
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|boolean
name|isGet
init|=
literal|"GET"
operator|.
name|equalsIgnoreCase
argument_list|(
operator|(
operator|(
name|HttpServletRequest
operator|)
name|request
operator|)
operator|.
name|getMethod
argument_list|()
argument_list|)
decl_stmt|;
name|ReceiveCommits
name|rc
init|=
operator|(
name|ReceiveCommits
operator|)
name|request
operator|.
name|getAttribute
argument_list|(
name|ATT_RC
argument_list|)
decl_stmt|;
name|ReceivePack
name|rp
init|=
name|rc
operator|.
name|getReceivePack
argument_list|()
decl_stmt|;
name|rp
operator|.
name|getAdvertiseRefsHook
argument_list|()
operator|.
name|advertiseRefs
argument_list|(
name|rp
argument_list|)
expr_stmt|;
name|ProjectControl
name|pc
init|=
operator|(
name|ProjectControl
operator|)
name|request
operator|.
name|getAttribute
argument_list|(
name|ATT_CONTROL
argument_list|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|projectName
init|=
name|pc
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|pc
operator|.
name|canRunReceivePack
argument_list|()
condition|)
block|{
name|GitSmartHttpTools
operator|.
name|sendError
argument_list|(
operator|(
name|HttpServletRequest
operator|)
name|request
argument_list|,
operator|(
name|HttpServletResponse
operator|)
name|response
argument_list|,
name|HttpServletResponse
operator|.
name|SC_FORBIDDEN
argument_list|,
literal|"receive-pack not permitted on this server"
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|Capable
name|s
init|=
name|rc
operator|.
name|canUpload
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|!=
name|Capable
operator|.
name|OK
condition|)
block|{
name|GitSmartHttpTools
operator|.
name|sendError
argument_list|(
operator|(
name|HttpServletRequest
operator|)
name|request
argument_list|,
operator|(
name|HttpServletResponse
operator|)
name|response
argument_list|,
name|HttpServletResponse
operator|.
name|SC_FORBIDDEN
argument_list|,
literal|"\n"
operator|+
name|s
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|rp
operator|.
name|isCheckReferencedObjectsAreReachable
argument_list|()
condition|)
block|{
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
operator|(
name|pc
operator|.
name|getCurrentUser
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
operator|)
condition|)
block|{
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
name|AdvertisedObjectsCacheKey
name|cacheKey
init|=
operator|new
name|AdvertisedObjectsCacheKey
argument_list|(
operator|(
operator|(
name|IdentifiedUser
operator|)
name|pc
operator|.
name|getCurrentUser
argument_list|()
operator|)
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|projectName
argument_list|)
decl_stmt|;
if|if
condition|(
name|isGet
condition|)
block|{
name|cache
operator|.
name|invalidate
argument_list|(
name|cacheKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|ids
init|=
name|cache
operator|.
name|getIfPresent
argument_list|(
name|cacheKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|ids
operator|!=
literal|null
condition|)
block|{
name|rp
operator|.
name|getAdvertisedObjects
argument_list|()
operator|.
name|addAll
argument_list|(
name|ids
argument_list|)
expr_stmt|;
name|cache
operator|.
name|invalidate
argument_list|(
name|cacheKey
argument_list|)
expr_stmt|;
block|}
block|}
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
if|if
condition|(
name|isGet
condition|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|cacheKey
argument_list|,
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|rp
operator|.
name|getAdvertisedObjects
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig arg0)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|arg0
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit

