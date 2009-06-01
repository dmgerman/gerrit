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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
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
name|HostPageServlet
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
name|Column
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
name|StringKey
import|;
end_import

begin_comment
comment|/** Global configuration needed to serve web requests. */
end_comment

begin_class
DECL|class|SystemConfig
specifier|public
specifier|final
class|class
name|SystemConfig
block|{
DECL|class|Key
specifier|public
specifier|static
specifier|final
class|class
name|Key
extends|extends
name|StringKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
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
DECL|field|VALUE
specifier|private
specifier|static
specifier|final
name|String
name|VALUE
init|=
literal|"X"
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|1
argument_list|)
DECL|field|one
specifier|protected
name|String
name|one
init|=
name|VALUE
decl_stmt|;
DECL|method|Key ()
specifier|public
name|Key
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|VALUE
return|;
block|}
annotation|@
name|Override
DECL|method|set (final String newValue)
specifier|protected
name|void
name|set
parameter_list|(
specifier|final
name|String
name|newValue
parameter_list|)
block|{
assert|assert
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|newValue
argument_list|)
assert|;
block|}
block|}
DECL|enum|LoginType
specifier|public
specifier|static
enum|enum
name|LoginType
block|{
comment|/** Login relies upon the OpenID standard: {@link "http://openid.net/"} */
DECL|enumConstant|OPENID
name|OPENID
block|,
comment|/**      * Login relies upon the container/web server security.      *<p>      * The container or web server must populate an HTTP header with the some      * user token. Gerrit will implicitly trust the value of this header to      * supply the unique identity.      */
DECL|enumConstant|HTTP
name|HTTP
block|;   }
comment|/** Construct a new, unconfigured instance. */
DECL|method|create ()
specifier|public
specifier|static
name|SystemConfig
name|create
parameter_list|()
block|{
specifier|final
name|SystemConfig
name|r
init|=
operator|new
name|SystemConfig
argument_list|()
decl_stmt|;
name|r
operator|.
name|singleton
operator|=
operator|new
name|SystemConfig
operator|.
name|Key
argument_list|()
expr_stmt|;
return|return
name|r
return|;
block|}
annotation|@
name|Column
DECL|field|singleton
specifier|protected
name|Key
name|singleton
decl_stmt|;
comment|/** Private key to sign XSRF protection tokens. */
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|36
argument_list|)
DECL|field|xsrfPrivateKey
specifier|public
specifier|transient
name|String
name|xsrfPrivateKey
decl_stmt|;
comment|/** Private key to sign account identification cookies. */
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|36
argument_list|)
DECL|field|accountPrivateKey
specifier|public
specifier|transient
name|String
name|accountPrivateKey
decl_stmt|;
comment|/** Maximum web session age, in seconds. */
annotation|@
name|Column
DECL|field|maxSessionAge
specifier|public
specifier|transient
name|int
name|maxSessionAge
decl_stmt|;
comment|/**    * Local filesystem location of header/footer/CSS configuration files    *     * @see HostPageServlet    */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|sitePath
specifier|public
specifier|transient
name|String
name|sitePath
decl_stmt|;
comment|/** Optional canonical URL for this application. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|canonicalUrl
specifier|public
name|String
name|canonicalUrl
decl_stmt|;
comment|/** Optional URL of a gitweb installation to also view changes through. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|gitwebUrl
specifier|public
name|String
name|gitwebUrl
decl_stmt|;
comment|/**    * Optional URL of the anonymous git daemon for project access.    *<p>    * For example:<code>git://host/base/</code>    */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|gitDaemonUrl
specifier|public
name|String
name|gitDaemonUrl
decl_stmt|;
comment|/** Local filesystem location all projects reside within. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|gitBasePath
specifier|public
specifier|transient
name|String
name|gitBasePath
decl_stmt|;
comment|/** Name this Gerrit instance calls itself when it makes changes in Git. */
annotation|@
name|Column
DECL|field|gerritGitName
specifier|public
name|String
name|gerritGitName
decl_stmt|;
comment|/** Email this Gerrit instance calls itself when it makes changes in Git. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|gerritGitEmail
specifier|public
name|String
name|gerritGitEmail
decl_stmt|;
comment|/** Type of login access used by this instance. */
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|16
argument_list|)
DECL|field|loginType
specifier|protected
name|String
name|loginType
decl_stmt|;
comment|/** HTTP header to use for the user identity if loginType is HTTP. */
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|30
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|loginHttpHeader
specifier|public
specifier|transient
name|String
name|loginHttpHeader
decl_stmt|;
comment|/** Format to generate email address from a login names */
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|30
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|emailFormat
specifier|public
specifier|transient
name|String
name|emailFormat
decl_stmt|;
comment|/**    * Can user accounts from Gerrit1 upgrade to use OpenID?    *<p>    * This setting should only be true if this server is an upgraded database    * from Gerrit1, and if there are still outstanding accounts which need to be    * upgraded to Gerrit2's OpenID authentication scheme. Any other system should    * leave this setting false.    */
annotation|@
name|Column
DECL|field|allowGoogleAccountUpgrade
specifier|public
specifier|transient
name|boolean
name|allowGoogleAccountUpgrade
decl_stmt|;
comment|/** Is a verified {@link AccountAgreement} required to upload changes? */
annotation|@
name|Column
DECL|field|useContributorAgreements
specifier|public
name|boolean
name|useContributorAgreements
decl_stmt|;
comment|/** Should Gerrit advertise 'repo download' for patch sets? */
annotation|@
name|Column
DECL|field|useRepoDownload
specifier|public
name|boolean
name|useRepoDownload
decl_stmt|;
comment|/** Identity of the administration group; those with full access. */
annotation|@
name|Column
DECL|field|adminGroupId
specifier|public
name|AccountGroup
operator|.
name|Id
name|adminGroupId
decl_stmt|;
comment|/** Identity of the anonymous group, which permits anyone. */
annotation|@
name|Column
DECL|field|anonymousGroupId
specifier|public
name|AccountGroup
operator|.
name|Id
name|anonymousGroupId
decl_stmt|;
comment|/** Identity of the registered users group, which permits anyone. */
annotation|@
name|Column
DECL|field|registeredGroupId
specifier|public
name|AccountGroup
operator|.
name|Id
name|registeredGroupId
decl_stmt|;
DECL|method|getLoginType ()
specifier|public
name|LoginType
name|getLoginType
parameter_list|()
block|{
return|return
name|loginType
operator|!=
literal|null
condition|?
name|LoginType
operator|.
name|valueOf
argument_list|(
name|loginType
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|setLoginType (final LoginType t)
specifier|public
name|void
name|setLoginType
parameter_list|(
specifier|final
name|LoginType
name|t
parameter_list|)
block|{
name|loginType
operator|=
name|t
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
DECL|method|SystemConfig ()
specifier|protected
name|SystemConfig
parameter_list|()
block|{   }
block|}
end_class

end_unit

