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
name|gerrit
operator|.
name|server
operator|.
name|CurrentUser
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
operator|.
name|AttributeKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
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

begin_comment
comment|/** Global data related to an active SSH connection. */
end_comment

begin_class
DECL|class|SshSession
specifier|public
class|class
name|SshSession
block|{
comment|/** ServerSession attribute key for this object instance. */
DECL|field|KEY
specifier|public
specifier|static
specifier|final
name|AttributeKey
argument_list|<
name|SshSession
argument_list|>
name|KEY
init|=
operator|new
name|AttributeKey
argument_list|<
name|SshSession
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|sessionId
specifier|private
specifier|final
name|int
name|sessionId
decl_stmt|;
DECL|field|remoteAddress
specifier|private
specifier|final
name|SocketAddress
name|remoteAddress
decl_stmt|;
DECL|field|remoteAsString
specifier|private
specifier|final
name|String
name|remoteAsString
decl_stmt|;
DECL|field|identity
specifier|private
specifier|volatile
name|CurrentUser
name|identity
decl_stmt|;
DECL|field|username
specifier|private
specifier|volatile
name|String
name|username
decl_stmt|;
DECL|field|authError
specifier|private
specifier|volatile
name|String
name|authError
decl_stmt|;
DECL|method|SshSession (final int sessionId, SocketAddress peer)
name|SshSession
parameter_list|(
specifier|final
name|int
name|sessionId
parameter_list|,
name|SocketAddress
name|peer
parameter_list|)
block|{
name|this
operator|.
name|sessionId
operator|=
name|sessionId
expr_stmt|;
name|this
operator|.
name|remoteAddress
operator|=
name|peer
expr_stmt|;
name|this
operator|.
name|remoteAsString
operator|=
name|format
argument_list|(
name|remoteAddress
argument_list|)
expr_stmt|;
block|}
DECL|method|SshSession (SshSession parent, SocketAddress peer, CurrentUser user)
name|SshSession
parameter_list|(
name|SshSession
name|parent
parameter_list|,
name|SocketAddress
name|peer
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|sessionId
operator|=
name|parent
operator|.
name|sessionId
expr_stmt|;
name|this
operator|.
name|remoteAddress
operator|=
name|peer
expr_stmt|;
if|if
condition|(
name|parent
operator|.
name|remoteAddress
operator|==
name|peer
condition|)
block|{
name|this
operator|.
name|remoteAsString
operator|=
name|parent
operator|.
name|remoteAsString
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|remoteAsString
operator|=
name|format
argument_list|(
name|peer
argument_list|)
operator|+
literal|"/"
operator|+
name|parent
operator|.
name|remoteAsString
expr_stmt|;
block|}
name|this
operator|.
name|identity
operator|=
name|user
expr_stmt|;
block|}
comment|/** Unique session number, assigned during connect. */
DECL|method|getSessionId ()
specifier|public
name|int
name|getSessionId
parameter_list|()
block|{
return|return
name|sessionId
return|;
block|}
comment|/** Identity of the authenticated user account on the socket. */
DECL|method|getCurrentUser ()
specifier|public
name|CurrentUser
name|getCurrentUser
parameter_list|()
block|{
return|return
name|identity
return|;
block|}
DECL|method|getUsername ()
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|getAuthenticationError ()
name|String
name|getAuthenticationError
parameter_list|()
block|{
return|return
name|authError
return|;
block|}
DECL|method|authenticationSuccess (String user, CurrentUser id)
name|void
name|authenticationSuccess
parameter_list|(
name|String
name|user
parameter_list|,
name|CurrentUser
name|id
parameter_list|)
block|{
name|username
operator|=
name|user
expr_stmt|;
name|identity
operator|=
name|id
expr_stmt|;
name|authError
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|authenticationError (String user, String error)
name|void
name|authenticationError
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|error
parameter_list|)
block|{
name|username
operator|=
name|user
expr_stmt|;
name|identity
operator|=
literal|null
expr_stmt|;
name|authError
operator|=
name|error
expr_stmt|;
block|}
comment|/** @return {@code true} if the authentication did not succeed. */
DECL|method|isAuthenticationError ()
name|boolean
name|isAuthenticationError
parameter_list|()
block|{
return|return
name|authError
operator|!=
literal|null
return|;
block|}
DECL|method|getRemoteAddress ()
name|SocketAddress
name|getRemoteAddress
parameter_list|()
block|{
return|return
name|remoteAddress
return|;
block|}
DECL|method|getRemoteAddressAsString ()
name|String
name|getRemoteAddressAsString
parameter_list|()
block|{
return|return
name|remoteAsString
return|;
block|}
DECL|method|format (final SocketAddress remote)
specifier|private
specifier|static
name|String
name|format
parameter_list|(
specifier|final
name|SocketAddress
name|remote
parameter_list|)
block|{
if|if
condition|(
name|remote
operator|instanceof
name|InetSocketAddress
condition|)
block|{
specifier|final
name|InetSocketAddress
name|sa
init|=
operator|(
name|InetSocketAddress
operator|)
name|remote
decl_stmt|;
specifier|final
name|InetAddress
name|in
init|=
name|sa
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
return|return
name|in
operator|.
name|getHostAddress
argument_list|()
return|;
block|}
specifier|final
name|String
name|hostName
init|=
name|sa
operator|.
name|getHostName
argument_list|()
decl_stmt|;
if|if
condition|(
name|hostName
operator|!=
literal|null
condition|)
block|{
return|return
name|hostName
return|;
block|}
block|}
return|return
name|remote
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

