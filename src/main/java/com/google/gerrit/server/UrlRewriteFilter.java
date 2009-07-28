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
name|client
operator|.
name|Link
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
name|client
operator|.
name|reviewdb
operator|.
name|Account
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
name|client
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
name|client
operator|.
name|reviewdb
operator|.
name|RevId
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
name|client
operator|.
name|reviewdb
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
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
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
name|HashMap
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
name|List
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
comment|/** Rewrites Gerrit 1 style URLs to Gerrit 2 style URLs. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|UrlRewriteFilter
specifier|public
class|class
name|UrlRewriteFilter
implements|implements
name|Filter
block|{
DECL|field|CHANGE_ID
specifier|private
specifier|static
specifier|final
name|Pattern
name|CHANGE_ID
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^/(\\d+)/?$"
argument_list|)
decl_stmt|;
DECL|field|REV_ID
specifier|private
specifier|static
specifier|final
name|Pattern
name|REV_ID
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^/r/([0-9a-fA-F]{4,"
operator|+
name|RevId
operator|.
name|LEN
operator|+
literal|"})/?$"
argument_list|)
decl_stmt|;
DECL|field|USER_PAGE
specifier|private
specifier|static
specifier|final
name|Pattern
name|USER_PAGE
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^/user/(.*)/?$"
argument_list|)
decl_stmt|;
DECL|field|staticLinks
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|staticLinks
decl_stmt|;
DECL|field|staticExtensions
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|staticExtensions
decl_stmt|;
static|static
block|{
name|staticLinks
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|staticLinks
operator|.
name|put
argument_list|(
literal|"/mine"
argument_list|,
name|Link
operator|.
name|MINE
argument_list|)
expr_stmt|;
name|staticLinks
operator|.
name|put
argument_list|(
literal|"/starred"
argument_list|,
name|Link
operator|.
name|MINE_STARRED
argument_list|)
expr_stmt|;
name|staticLinks
operator|.
name|put
argument_list|(
literal|"/settings"
argument_list|,
name|Link
operator|.
name|SETTINGS
argument_list|)
expr_stmt|;
name|staticLinks
operator|.
name|put
argument_list|(
literal|"/all_unclaimed"
argument_list|,
name|Link
operator|.
name|ALL_UNCLAIMED
argument_list|)
expr_stmt|;
name|staticLinks
operator|.
name|put
argument_list|(
literal|"/all"
argument_list|,
name|Link
operator|.
name|ALL_MERGED
argument_list|)
expr_stmt|;
name|staticLinks
operator|.
name|put
argument_list|(
literal|"/open"
argument_list|,
name|Link
operator|.
name|ALL_OPEN
argument_list|)
expr_stmt|;
name|staticExtensions
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|staticExtensions
operator|.
name|add
argument_list|(
literal|".css"
argument_list|)
expr_stmt|;
name|staticExtensions
operator|.
name|add
argument_list|(
literal|".gif"
argument_list|)
expr_stmt|;
name|staticExtensions
operator|.
name|add
argument_list|(
literal|".html"
argument_list|)
expr_stmt|;
name|staticExtensions
operator|.
name|add
argument_list|(
literal|".js"
argument_list|)
expr_stmt|;
name|staticExtensions
operator|.
name|add
argument_list|(
literal|".png"
argument_list|)
expr_stmt|;
block|}
DECL|field|server
specifier|private
specifier|final
name|GerritServer
name|server
decl_stmt|;
DECL|field|config
specifier|private
name|FilterConfig
name|config
decl_stmt|;
annotation|@
name|Inject
DECL|method|UrlRewriteFilter (final GerritServer gs)
name|UrlRewriteFilter
parameter_list|(
specifier|final
name|GerritServer
name|gs
parameter_list|)
block|{
name|server
operator|=
name|gs
expr_stmt|;
block|}
DECL|method|init (final FilterConfig config)
specifier|public
name|void
name|init
parameter_list|(
specifier|final
name|FilterConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{   }
DECL|method|doFilter (final ServletRequest sreq, final ServletResponse srsp, final FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
specifier|final
name|ServletRequest
name|sreq
parameter_list|,
specifier|final
name|ServletResponse
name|srsp
parameter_list|,
specifier|final
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
specifier|final
name|HttpServletRequest
name|req
init|=
operator|(
name|HttpServletRequest
operator|)
name|sreq
decl_stmt|;
specifier|final
name|HttpServletResponse
name|rsp
init|=
operator|(
name|HttpServletResponse
operator|)
name|srsp
decl_stmt|;
specifier|final
name|String
name|pathInfo
init|=
name|pathInfo
argument_list|(
name|req
argument_list|)
decl_stmt|;
if|if
condition|(
name|pathInfo
operator|.
name|startsWith
argument_list|(
literal|"/gerrit/rpc/"
argument_list|)
condition|)
block|{
comment|// RPC requests are very common in Gerrit 2, we want to make sure
comment|// they run quickly by jumping through the chain as fast as we can.
comment|//
name|chain
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|staticExtension
argument_list|(
name|pathInfo
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|,
name|chain
argument_list|)
condition|)
block|{     }
elseif|else
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|pathInfo
argument_list|)
operator|||
literal|"/"
operator|.
name|equals
argument_list|(
name|pathInfo
argument_list|)
condition|)
block|{
name|req
operator|.
name|getRequestDispatcher
argument_list|(
literal|"/Gerrit"
argument_list|)
operator|.
name|forward
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|staticLink
argument_list|(
name|pathInfo
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
condition|)
block|{     }
elseif|else
if|if
condition|(
name|bareChangeId
argument_list|(
name|pathInfo
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
condition|)
block|{     }
elseif|else
if|if
condition|(
name|bareRevisionId
argument_list|(
name|pathInfo
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
condition|)
block|{     }
elseif|else
if|if
condition|(
name|bareUserEmailDashboard
argument_list|(
name|pathInfo
argument_list|,
name|req
argument_list|,
name|rsp
argument_list|)
condition|)
block|{     }
else|else
block|{
comment|// Anything else is either a static resource request (which the container
comment|// can do for us) or is going to be a 404 error when the container cannot
comment|// find the resource. Either form of request is not very common compared
comment|// to the above cases.
comment|//
name|chain
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|staticExtension (final String pathInfo, final HttpServletRequest req, final HttpServletResponse rsp, final FilterChain chain)
specifier|private
specifier|static
name|boolean
name|staticExtension
parameter_list|(
specifier|final
name|String
name|pathInfo
parameter_list|,
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|rsp
parameter_list|,
specifier|final
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
specifier|final
name|int
name|d
init|=
name|pathInfo
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|>
literal|0
operator|&&
name|staticExtensions
operator|.
name|contains
argument_list|(
name|pathInfo
operator|.
name|substring
argument_list|(
name|d
operator|+
literal|1
argument_list|)
argument_list|)
condition|)
block|{
comment|// Any URL which ends in this static extension is meant to be handled
comment|// by the servlet container, by returning a resource from the WAR.
comment|// We don't need to evaluate it any further.
comment|//
name|chain
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|staticLink (final String pathInfo, final HttpServletRequest req, final HttpServletResponse rsp)
specifier|private
specifier|static
name|boolean
name|staticLink
parameter_list|(
specifier|final
name|String
name|pathInfo
parameter_list|,
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|String
name|newLink
init|=
name|staticLinks
operator|.
name|get
argument_list|(
name|pathInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|newLink
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// A static link (one with no parameters).
comment|//
specifier|final
name|StringBuffer
name|url
init|=
name|toGerrit
argument_list|(
name|req
argument_list|)
decl_stmt|;
if|if
condition|(
name|newLink
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|url
operator|.
name|append
argument_list|(
literal|'#'
argument_list|)
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
name|newLink
argument_list|)
expr_stmt|;
block|}
name|rsp
operator|.
name|sendRedirect
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|bareChangeId (final String pathInfo, final HttpServletRequest req, final HttpServletResponse rsp)
specifier|private
specifier|static
name|boolean
name|bareChangeId
parameter_list|(
specifier|final
name|String
name|pathInfo
parameter_list|,
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Matcher
name|m
init|=
name|CHANGE_ID
operator|.
name|matcher
argument_list|(
name|pathInfo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|Change
operator|.
name|Id
name|id
init|=
name|Change
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|StringBuffer
name|url
init|=
name|toGerrit
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|url
operator|.
name|append
argument_list|(
literal|'#'
argument_list|)
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
name|Link
operator|.
name|toChange
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendRedirect
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|bareRevisionId (final String pathInfo, final HttpServletRequest req, final HttpServletResponse rsp)
specifier|private
name|boolean
name|bareRevisionId
parameter_list|(
specifier|final
name|String
name|pathInfo
parameter_list|,
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Matcher
name|m
init|=
name|REV_ID
operator|.
name|matcher
argument_list|(
name|pathInfo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|String
name|rev
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|rev
operator|.
name|length
argument_list|()
operator|>
name|RevId
operator|.
name|LEN
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|final
name|StringBuffer
name|url
init|=
name|toGerrit
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|url
operator|.
name|append
argument_list|(
literal|'#'
argument_list|)
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
name|Link
operator|.
name|toChangeQuery
argument_list|(
name|rev
argument_list|)
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendRedirect
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|bareUserEmailDashboard (final String pathInfo, final HttpServletRequest req, final HttpServletResponse rsp)
specifier|private
name|boolean
name|bareUserEmailDashboard
parameter_list|(
specifier|final
name|String
name|pathInfo
parameter_list|,
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Matcher
name|m
init|=
name|USER_PAGE
operator|.
name|matcher
argument_list|(
name|pathInfo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|String
name|email
init|=
name|cleanEmail
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Account
argument_list|>
name|people
decl_stmt|;
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|server
operator|.
name|getSchemaFactory
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
name|people
operator|=
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|byPreferredEmail
argument_list|(
name|email
argument_list|)
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|config
operator|.
name|getServletContext
argument_list|()
operator|.
name|log
argument_list|(
literal|"Unable to query for "
operator|+
name|email
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|people
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|people
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
specifier|final
name|StringBuffer
name|url
init|=
name|toGerrit
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|url
operator|.
name|append
argument_list|(
literal|'#'
argument_list|)
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
name|Link
operator|.
name|toAccountDashboard
argument_list|(
name|people
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendRedirect
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO Someday this should be a menu of choices.
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|cleanEmail (final String email)
specifier|private
specifier|static
name|String
name|cleanEmail
parameter_list|(
specifier|final
name|String
name|email
parameter_list|)
block|{
name|int
name|dc
init|=
name|email
operator|.
name|indexOf
argument_list|(
literal|",,"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dc
operator|>=
literal|0
condition|)
block|{
return|return
name|email
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dc
argument_list|)
operator|+
literal|"@"
operator|+
name|email
operator|.
name|substring
argument_list|(
name|dc
operator|+
literal|2
argument_list|)
return|;
block|}
name|dc
operator|=
name|email
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
expr_stmt|;
if|if
condition|(
name|dc
operator|>=
literal|0
condition|)
block|{
return|return
name|email
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dc
argument_list|)
operator|+
literal|"@"
operator|+
name|email
operator|.
name|substring
argument_list|(
name|dc
operator|+
literal|1
argument_list|)
return|;
block|}
return|return
name|email
return|;
block|}
DECL|method|pathInfo (final HttpServletRequest req)
specifier|private
specifier|static
name|String
name|pathInfo
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|)
block|{
specifier|final
name|String
name|uri
init|=
name|req
operator|.
name|getRequestURI
argument_list|()
decl_stmt|;
specifier|final
name|String
name|ctx
init|=
name|req
operator|.
name|getContextPath
argument_list|()
decl_stmt|;
return|return
name|uri
operator|.
name|startsWith
argument_list|(
name|ctx
argument_list|)
condition|?
name|uri
operator|.
name|substring
argument_list|(
name|ctx
operator|.
name|length
argument_list|()
argument_list|)
else|:
name|uri
return|;
block|}
DECL|method|toGerrit (final HttpServletRequest req)
specifier|private
specifier|static
name|StringBuffer
name|toGerrit
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|)
block|{
specifier|final
name|StringBuffer
name|url
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|url
operator|.
name|append
argument_list|(
name|req
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
return|return
name|url
return|;
block|}
block|}
end_class

end_unit

