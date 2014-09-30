begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
package|;
end_package

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
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|SessionListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|PublickeyAuthenticator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|session
operator|.
name|ServerSession
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
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
name|ConcurrentHashMap
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|CachingPublicKeyAuthenticator
specifier|public
class|class
name|CachingPublicKeyAuthenticator
implements|implements
name|PublickeyAuthenticator
implements|,
name|SessionListener
block|{
DECL|field|authenticator
specifier|private
specifier|final
name|PublickeyAuthenticator
name|authenticator
decl_stmt|;
DECL|field|sessionCache
specifier|private
specifier|final
name|Map
argument_list|<
name|ServerSession
argument_list|,
name|Map
argument_list|<
name|PublicKey
argument_list|,
name|Boolean
argument_list|>
argument_list|>
name|sessionCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|CachingPublicKeyAuthenticator (DatabasePubKeyAuth authenticator)
specifier|public
name|CachingPublicKeyAuthenticator
parameter_list|(
name|DatabasePubKeyAuth
name|authenticator
parameter_list|)
block|{
name|this
operator|.
name|authenticator
operator|=
name|authenticator
expr_stmt|;
name|this
operator|.
name|sessionCache
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|authenticate (String username, PublicKey key, ServerSession session)
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|String
name|username
parameter_list|,
name|PublicKey
name|key
parameter_list|,
name|ServerSession
name|session
parameter_list|)
block|{
name|Map
argument_list|<
name|PublicKey
argument_list|,
name|Boolean
argument_list|>
name|m
init|=
name|sessionCache
operator|.
name|get
argument_list|(
name|session
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|m
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|sessionCache
operator|.
name|put
argument_list|(
name|session
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|session
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|m
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
name|boolean
name|r
init|=
name|authenticator
operator|.
name|authenticate
argument_list|(
name|username
argument_list|,
name|key
argument_list|,
name|session
argument_list|)
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|r
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
annotation|@
name|Override
DECL|method|sessionCreated (Session session)
specifier|public
name|void
name|sessionCreated
parameter_list|(
name|Session
name|session
parameter_list|)
block|{   }
annotation|@
name|Override
DECL|method|sessionEvent (Session sesssion, Event event)
specifier|public
name|void
name|sessionEvent
parameter_list|(
name|Session
name|sesssion
parameter_list|,
name|Event
name|event
parameter_list|)
block|{   }
annotation|@
name|Override
DECL|method|sessionClosed (Session session)
specifier|public
name|void
name|sessionClosed
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|sessionCache
operator|.
name|remove
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

