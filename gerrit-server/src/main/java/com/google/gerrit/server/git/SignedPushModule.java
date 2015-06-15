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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|server
operator|.
name|util
operator|.
name|BouncyCastleUtil
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
name|Singleton
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
name|PreReceiveHookChain
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

begin_class
DECL|class|SignedPushModule
specifier|public
class|class
name|SignedPushModule
extends|extends
name|AbstractModule
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SignedPushModule
operator|.
name|class
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
if|if
condition|(
name|BouncyCastleUtil
operator|.
name|havePGP
argument_list|()
condition|)
block|{
name|DynamicSet
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|ReceivePackInitializer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|Initializer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"BouncyCastle PGP not installed; signed push verification is"
operator|+
literal|" disabled"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|Initializer
specifier|private
specifier|static
class|class
name|Initializer
implements|implements
name|ReceivePackInitializer
block|{
DECL|field|hook
specifier|private
specifier|final
name|SignedPushPreReceiveHook
name|hook
decl_stmt|;
annotation|@
name|Inject
DECL|method|Initializer (SignedPushPreReceiveHook hook)
name|Initializer
parameter_list|(
name|SignedPushPreReceiveHook
name|hook
parameter_list|)
block|{
name|this
operator|.
name|hook
operator|=
name|hook
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (Project.NameKey project, ReceivePack rp)
specifier|public
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
name|rp
operator|.
name|setPreReceiveHook
argument_list|(
name|PreReceiveHookChain
operator|.
name|newChain
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|hook
argument_list|,
name|rp
operator|.
name|getPreReceiveHook
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

