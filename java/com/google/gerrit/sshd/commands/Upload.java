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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
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
name|UploadPackInitializer
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
name|UploadValidationException
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
name|permissions
operator|.
name|PermissionBackendException
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
name|ProjectPermission
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
name|sshd
operator|.
name|AbstractGitCommand
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
name|sshd
operator|.
name|SshSession
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
name|transport
operator|.
name|PostUploadHook
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
name|PostUploadHookChain
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
name|UploadPack
import|;
end_import

begin_comment
comment|/** Publishes Git repositories over SSH using the Git upload-pack protocol. */
end_comment

begin_class
DECL|class|Upload
specifier|final
class|class
name|Upload
extends|extends
name|AbstractGitCommand
block|{
DECL|field|config
annotation|@
name|Inject
specifier|private
name|TransferConfig
name|config
decl_stmt|;
DECL|field|refFilterFactory
annotation|@
name|Inject
specifier|private
name|VisibleRefFilter
operator|.
name|Factory
name|refFilterFactory
decl_stmt|;
DECL|field|preUploadHooks
annotation|@
name|Inject
specifier|private
name|DynamicSet
argument_list|<
name|PreUploadHook
argument_list|>
name|preUploadHooks
decl_stmt|;
DECL|field|postUploadHooks
annotation|@
name|Inject
specifier|private
name|DynamicSet
argument_list|<
name|PostUploadHook
argument_list|>
name|postUploadHooks
decl_stmt|;
DECL|field|uploadPackInitializers
annotation|@
name|Inject
specifier|private
name|DynamicSet
argument_list|<
name|UploadPackInitializer
argument_list|>
name|uploadPackInitializers
decl_stmt|;
DECL|field|uploadValidatorsFactory
annotation|@
name|Inject
specifier|private
name|UploadValidators
operator|.
name|Factory
name|uploadValidatorsFactory
decl_stmt|;
DECL|field|session
annotation|@
name|Inject
specifier|private
name|SshSession
name|session
decl_stmt|;
DECL|field|permissionBackend
annotation|@
name|Inject
specifier|private
name|PermissionBackend
name|permissionBackend
decl_stmt|;
annotation|@
name|Override
DECL|method|runImpl ()
specifier|protected
name|void
name|runImpl
parameter_list|()
throws|throws
name|IOException
throws|,
name|Failure
block|{
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|project
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|ProjectPermission
operator|.
name|RUN_UPLOAD_PACK
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: upload-pack not permitted on this server"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: unable to check permissions "
operator|+
name|e
argument_list|)
throw|;
block|}
specifier|final
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
name|setAdvertiseRefsHook
argument_list|(
name|refFilterFactory
operator|.
name|create
argument_list|(
name|projectState
argument_list|,
name|repo
argument_list|)
argument_list|)
expr_stmt|;
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
name|setPostUploadHook
argument_list|(
name|PostUploadHookChain
operator|.
name|newChain
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|postUploadHooks
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PreUploadHook
argument_list|>
name|allPreUploadHooks
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|preUploadHooks
argument_list|)
decl_stmt|;
name|allPreUploadHooks
operator|.
name|add
argument_list|(
name|uploadValidatorsFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|repo
argument_list|,
name|session
operator|.
name|getRemoteAddressAsString
argument_list|()
argument_list|)
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
name|allPreUploadHooks
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|UploadPackInitializer
name|initializer
range|:
name|uploadPackInitializers
control|)
block|{
name|initializer
operator|.
name|init
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|up
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|up
operator|.
name|upload
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
name|session
operator|.
name|setPeerAgent
argument_list|(
name|up
operator|.
name|getPeerUserAgent
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UploadValidationException
name|e
parameter_list|)
block|{
comment|// UploadValidationException is used by the UploadValidators to
comment|// stop the uploadPack. We do not want this exception to go beyond this
comment|// point otherwise it would print a stacktrace in the logs and return an
comment|// internal server error to the client.
if|if
condition|(
operator|!
name|e
operator|.
name|isOutput
argument_list|()
condition|)
block|{
name|up
operator|.
name|sendMessage
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit
