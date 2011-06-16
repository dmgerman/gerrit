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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
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
name|AccountProjectWatch
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
name|Change
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
name|config
operator|.
name|AuthConfig
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|Collections
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

begin_comment
comment|/** Identity of a peer daemon process that isn't this JVM. */
end_comment

begin_class
DECL|class|PeerDaemonUser
specifier|public
class|class
name|PeerDaemonUser
extends|extends
name|CurrentUser
block|{
comment|/** Magic username used by peers when they authenticate. */
DECL|field|USER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|USER_NAME
init|=
literal|"Gerrit Code Review"
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (@ssisted SocketAddress peer)
name|PeerDaemonUser
name|create
parameter_list|(
annotation|@
name|Assisted
name|SocketAddress
name|peer
parameter_list|)
function_decl|;
block|}
DECL|field|peer
specifier|private
specifier|final
name|SocketAddress
name|peer
decl_stmt|;
annotation|@
name|Inject
DECL|method|PeerDaemonUser (CapabilityControl.Factory capabilityControlFactory, AuthConfig authConfig, @Assisted SocketAddress peer)
specifier|protected
name|PeerDaemonUser
parameter_list|(
name|CapabilityControl
operator|.
name|Factory
name|capabilityControlFactory
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|,
annotation|@
name|Assisted
name|SocketAddress
name|peer
parameter_list|)
block|{
name|super
argument_list|(
name|capabilityControlFactory
argument_list|,
name|AccessPath
operator|.
name|SSH_COMMAND
argument_list|,
name|authConfig
argument_list|)
expr_stmt|;
name|this
operator|.
name|peer
operator|=
name|peer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEffectiveGroups ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getEffectiveGroups
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getStarredChanges ()
specifier|public
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|getStarredChanges
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getNotificationFilters ()
specifier|public
name|Collection
argument_list|<
name|AccountProjectWatch
argument_list|>
name|getNotificationFilters
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|getRemoteAddress ()
specifier|public
name|SocketAddress
name|getRemoteAddress
parameter_list|()
block|{
return|return
name|peer
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"PeerDaemon[address "
operator|+
name|getRemoteAddress
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

