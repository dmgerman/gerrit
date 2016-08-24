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
DECL|package|com.google.gerrit.acceptance.rest.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|change
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
operator|.
name|HttpHeaders
operator|.
name|ACCESS_CONTROL_ALLOW_CREDENTIALS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
operator|.
name|HttpHeaders
operator|.
name|ACCESS_CONTROL_ALLOW_HEADERS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
operator|.
name|HttpHeaders
operator|.
name|ACCESS_CONTROL_ALLOW_METHODS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
operator|.
name|HttpHeaders
operator|.
name|ACCESS_CONTROL_ALLOW_ORIGIN
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
operator|.
name|HttpHeaders
operator|.
name|ACCESS_CONTROL_REQUEST_HEADERS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
operator|.
name|HttpHeaders
operator|.
name|ACCESS_CONTROL_REQUEST_METHOD
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
operator|.
name|HttpHeaders
operator|.
name|ORIGIN
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|ImmutableList
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|PushOneCommit
operator|.
name|Result
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
name|acceptance
operator|.
name|RestResponse
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
name|testutil
operator|.
name|ConfigSuite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|fluent
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|BasicHeader
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
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CorsIT
specifier|public
class|class
name|CorsIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Default
DECL|method|allowExampleDotCom ()
specifier|public
specifier|static
name|Config
name|allowExampleDotCom
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setStringList
argument_list|(
literal|"site"
argument_list|,
literal|null
argument_list|,
literal|"allowOriginRegex"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"https?://(.+[.])?example[.]com"
argument_list|,
literal|"http://friend[.]ly"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|Test
DECL|method|origin ()
specifier|public
name|void
name|origin
parameter_list|()
throws|throws
name|Exception
block|{
name|Result
name|change
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|url
init|=
literal|"/changes/"
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
operator|+
literal|"/detail"
decl_stmt|;
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|get
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
operator|.
name|getHeader
argument_list|(
name|ACCESS_CONTROL_ALLOW_ORIGIN
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
operator|.
name|getHeader
argument_list|(
name|ACCESS_CONTROL_ALLOW_CREDENTIALS
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
name|url
argument_list|,
literal|true
argument_list|,
literal|"http://example.com"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|url
argument_list|,
literal|true
argument_list|,
literal|"https://sub.example.com"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|url
argument_list|,
literal|true
argument_list|,
literal|"http://friend.ly"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|url
argument_list|,
literal|false
argument_list|,
literal|"http://evil.attacker"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|url
argument_list|,
literal|false
argument_list|,
literal|"http://friendsly"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|putWithOriginRefused ()
specifier|public
name|void
name|putWithOriginRefused
parameter_list|()
throws|throws
name|Exception
block|{
name|Result
name|change
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|origin
init|=
literal|"http://example.com"
decl_stmt|;
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|putWithHeader
argument_list|(
literal|"/changes/"
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
operator|+
literal|"/topic"
argument_list|,
operator|new
name|BasicHeader
argument_list|(
name|ORIGIN
argument_list|,
name|origin
argument_list|)
argument_list|,
literal|"A"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|checkCors
argument_list|(
name|r
argument_list|,
literal|false
argument_list|,
name|origin
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|preflightOk ()
specifier|public
name|void
name|preflightOk
parameter_list|()
throws|throws
name|Exception
block|{
name|Result
name|change
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|origin
init|=
literal|"http://example.com"
decl_stmt|;
name|Request
name|req
init|=
name|Request
operator|.
name|Options
argument_list|(
name|adminRestSession
operator|.
name|url
argument_list|()
operator|+
literal|"/a/changes/"
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
operator|+
literal|"/detail"
argument_list|)
decl_stmt|;
name|req
operator|.
name|addHeader
argument_list|(
name|ORIGIN
argument_list|,
name|origin
argument_list|)
expr_stmt|;
name|req
operator|.
name|addHeader
argument_list|(
name|ACCESS_CONTROL_REQUEST_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|req
operator|.
name|addHeader
argument_list|(
name|ACCESS_CONTROL_REQUEST_HEADERS
argument_list|,
literal|"X-Requested-With"
argument_list|)
expr_stmt|;
name|RestResponse
name|res
init|=
name|adminRestSession
operator|.
name|execute
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|res
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|checkCors
argument_list|(
name|res
argument_list|,
literal|true
argument_list|,
name|origin
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|preflightBadOrigin ()
specifier|public
name|void
name|preflightBadOrigin
parameter_list|()
throws|throws
name|Exception
block|{
name|Result
name|change
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Request
name|req
init|=
name|Request
operator|.
name|Options
argument_list|(
name|adminRestSession
operator|.
name|url
argument_list|()
operator|+
literal|"/a/changes/"
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
operator|+
literal|"/detail"
argument_list|)
decl_stmt|;
name|req
operator|.
name|addHeader
argument_list|(
name|ORIGIN
argument_list|,
literal|"http://evil.attacker"
argument_list|)
expr_stmt|;
name|req
operator|.
name|addHeader
argument_list|(
name|ACCESS_CONTROL_REQUEST_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|adminRestSession
operator|.
name|execute
argument_list|(
name|req
argument_list|)
operator|.
name|assertBadRequest
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|preflightBadMethod ()
specifier|public
name|void
name|preflightBadMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Result
name|change
init|=
name|createChange
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|method
range|:
operator|new
name|String
index|[]
block|{
literal|"POST"
block|,
literal|"PUT"
block|,
literal|"DELETE"
block|,
literal|"PATCH"
block|}
control|)
block|{
name|Request
name|req
init|=
name|Request
operator|.
name|Options
argument_list|(
name|adminRestSession
operator|.
name|url
argument_list|()
operator|+
literal|"/a/changes/"
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
operator|+
literal|"/detail"
argument_list|)
decl_stmt|;
name|req
operator|.
name|addHeader
argument_list|(
name|ORIGIN
argument_list|,
literal|"http://example.com"
argument_list|)
expr_stmt|;
name|req
operator|.
name|addHeader
argument_list|(
name|ACCESS_CONTROL_REQUEST_METHOD
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|adminRestSession
operator|.
name|execute
argument_list|(
name|req
argument_list|)
operator|.
name|assertBadRequest
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|preflightBadHeader ()
specifier|public
name|void
name|preflightBadHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Result
name|change
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Request
name|req
init|=
name|Request
operator|.
name|Options
argument_list|(
name|adminRestSession
operator|.
name|url
argument_list|()
operator|+
literal|"/a/changes/"
operator|+
name|change
operator|.
name|getChangeId
argument_list|()
operator|+
literal|"/detail"
argument_list|)
decl_stmt|;
name|req
operator|.
name|addHeader
argument_list|(
name|ORIGIN
argument_list|,
literal|"http://example.com"
argument_list|)
expr_stmt|;
name|req
operator|.
name|addHeader
argument_list|(
name|ACCESS_CONTROL_REQUEST_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|req
operator|.
name|addHeader
argument_list|(
name|ACCESS_CONTROL_REQUEST_HEADERS
argument_list|,
literal|"X-Gerrit-Auth"
argument_list|)
expr_stmt|;
name|adminRestSession
operator|.
name|execute
argument_list|(
name|req
argument_list|)
operator|.
name|assertBadRequest
argument_list|()
expr_stmt|;
block|}
DECL|method|check (String url, boolean accept, String origin)
specifier|private
name|RestResponse
name|check
parameter_list|(
name|String
name|url
parameter_list|,
name|boolean
name|accept
parameter_list|,
name|String
name|origin
parameter_list|)
throws|throws
name|Exception
block|{
name|Header
name|hdr
init|=
operator|new
name|BasicHeader
argument_list|(
name|ORIGIN
argument_list|,
name|origin
argument_list|)
decl_stmt|;
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|getWithHeader
argument_list|(
name|url
argument_list|,
name|hdr
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|checkCors
argument_list|(
name|r
argument_list|,
name|accept
argument_list|,
name|origin
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|method|checkCors (RestResponse r, boolean accept, String origin)
specifier|private
name|void
name|checkCors
parameter_list|(
name|RestResponse
name|r
parameter_list|,
name|boolean
name|accept
parameter_list|,
name|String
name|origin
parameter_list|)
block|{
name|String
name|allowOrigin
init|=
name|r
operator|.
name|getHeader
argument_list|(
name|ACCESS_CONTROL_ALLOW_ORIGIN
argument_list|)
decl_stmt|;
name|String
name|allowCred
init|=
name|r
operator|.
name|getHeader
argument_list|(
name|ACCESS_CONTROL_ALLOW_CREDENTIALS
argument_list|)
decl_stmt|;
name|String
name|allowMethods
init|=
name|r
operator|.
name|getHeader
argument_list|(
name|ACCESS_CONTROL_ALLOW_METHODS
argument_list|)
decl_stmt|;
name|String
name|allowHeaders
init|=
name|r
operator|.
name|getHeader
argument_list|(
name|ACCESS_CONTROL_ALLOW_HEADERS
argument_list|)
decl_stmt|;
if|if
condition|(
name|accept
condition|)
block|{
name|assertThat
argument_list|(
name|allowOrigin
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|origin
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|allowCred
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|allowMethods
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"GET, OPTIONS"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|allowHeaders
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"X-Requested-With"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertThat
argument_list|(
name|allowOrigin
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|allowCred
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|allowMethods
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|allowHeaders
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

