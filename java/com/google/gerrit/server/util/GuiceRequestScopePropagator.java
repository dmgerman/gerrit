begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
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
name|server
operator|.
name|RemotePeer
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
name|servlet
operator|.
name|ServletScopes
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
name|util
operator|.
name|Providers
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
name|util
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Callable
import|;
end_import

begin_comment
comment|/** Propagator for Guice's built-in servlet scope. */
end_comment

begin_class
DECL|class|GuiceRequestScopePropagator
specifier|public
class|class
name|GuiceRequestScopePropagator
extends|extends
name|RequestScopePropagator
block|{
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|peer
specifier|private
specifier|final
name|SocketAddress
name|peer
decl_stmt|;
annotation|@
name|Inject
DECL|method|GuiceRequestScopePropagator ( @anonicalWebUrl @ullable Provider<String> urlProvider, @RemotePeer Provider<SocketAddress> remotePeerProvider, ThreadLocalRequestContext local)
name|GuiceRequestScopePropagator
parameter_list|(
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
annotation|@
name|RemotePeer
name|Provider
argument_list|<
name|SocketAddress
argument_list|>
name|remotePeerProvider
parameter_list|,
name|ThreadLocalRequestContext
name|local
parameter_list|)
block|{
name|super
argument_list|(
name|ServletScopes
operator|.
name|REQUEST
argument_list|,
name|local
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|urlProvider
operator|!=
literal|null
condition|?
name|urlProvider
operator|.
name|get
argument_list|()
else|:
literal|null
expr_stmt|;
name|this
operator|.
name|peer
operator|=
name|remotePeerProvider
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
comment|/** @see RequestScopePropagator#wrap(Callable) */
comment|// ServletScopes#continueRequest is deprecated, but it's not obvious their
comment|// recommended replacement is an appropriate drop-in solution; see
comment|// https://gerrit-review.googlesource.com/83971
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Override
DECL|method|wrapImpl (Callable<T> callable)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|Callable
argument_list|<
name|T
argument_list|>
name|wrapImpl
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|)
block|{
name|Map
argument_list|<
name|Key
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|seedMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Request scopes appear to use specific keys in their map, instead of only
comment|// providers. Add bindings for both the key to the instance directly and the
comment|// provider to the instance to be safe.
name|seedMap
operator|.
name|put
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|typeOfProvider
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|CanonicalWebUrl
operator|.
name|class
argument_list|)
argument_list|,
name|Providers
operator|.
name|of
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
name|seedMap
operator|.
name|put
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|CanonicalWebUrl
operator|.
name|class
argument_list|)
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|seedMap
operator|.
name|put
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|typeOfProvider
argument_list|(
name|SocketAddress
operator|.
name|class
argument_list|)
argument_list|,
name|RemotePeer
operator|.
name|class
argument_list|)
argument_list|,
name|Providers
operator|.
name|of
argument_list|(
name|peer
argument_list|)
argument_list|)
expr_stmt|;
name|seedMap
operator|.
name|put
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|SocketAddress
operator|.
name|class
argument_list|,
name|RemotePeer
operator|.
name|class
argument_list|)
argument_list|,
name|peer
argument_list|)
expr_stmt|;
return|return
name|ServletScopes
operator|.
name|continueRequest
argument_list|(
name|callable
argument_list|,
name|seedMap
argument_list|)
return|;
block|}
DECL|method|typeOfProvider (Type type)
specifier|private
name|ParameterizedType
name|typeOfProvider
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
return|return
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|Provider
operator|.
name|class
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

