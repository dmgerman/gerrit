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
DECL|package|com.google.gerrit.util.http
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|util
operator|.
name|http
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
name|gerrit
operator|.
name|util
operator|.
name|http
operator|.
name|testutil
operator|.
name|FakeHttpServletRequest
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
DECL|class|RequestUtilTest
specifier|public
class|class
name|RequestUtilTest
block|{
annotation|@
name|Test
DECL|method|emptyContextPath ()
specifier|public
name|void
name|emptyContextPath
parameter_list|()
block|{
name|assertThat
argument_list|(
name|RequestUtil
operator|.
name|getEncodedPathInfo
argument_list|(
name|fakeRequest
argument_list|(
literal|""
argument_list|,
literal|"/s"
argument_list|,
literal|"/foo/bar"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"/foo/bar"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|RequestUtil
operator|.
name|getEncodedPathInfo
argument_list|(
name|fakeRequest
argument_list|(
literal|""
argument_list|,
literal|"/s"
argument_list|,
literal|"/foo%2Fbar"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"/foo%2Fbar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|emptyServletPath ()
specifier|public
name|void
name|emptyServletPath
parameter_list|()
block|{
name|assertThat
argument_list|(
name|RequestUtil
operator|.
name|getEncodedPathInfo
argument_list|(
name|fakeRequest
argument_list|(
literal|""
argument_list|,
literal|"/c"
argument_list|,
literal|"/foo/bar"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"/foo/bar"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|RequestUtil
operator|.
name|getEncodedPathInfo
argument_list|(
name|fakeRequest
argument_list|(
literal|""
argument_list|,
literal|"/c"
argument_list|,
literal|"/foo%2Fbar"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"/foo%2Fbar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|trailingSlashes ()
specifier|public
name|void
name|trailingSlashes
parameter_list|()
block|{
name|assertThat
argument_list|(
name|RequestUtil
operator|.
name|getEncodedPathInfo
argument_list|(
name|fakeRequest
argument_list|(
literal|"/c"
argument_list|,
literal|"/s"
argument_list|,
literal|"/foo/bar/"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"/foo/bar/"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|RequestUtil
operator|.
name|getEncodedPathInfo
argument_list|(
name|fakeRequest
argument_list|(
literal|"/c"
argument_list|,
literal|"/s"
argument_list|,
literal|"/foo/bar///"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"/foo/bar/"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|RequestUtil
operator|.
name|getEncodedPathInfo
argument_list|(
name|fakeRequest
argument_list|(
literal|"/c"
argument_list|,
literal|"/s"
argument_list|,
literal|"/foo%2Fbar/"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"/foo%2Fbar/"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|RequestUtil
operator|.
name|getEncodedPathInfo
argument_list|(
name|fakeRequest
argument_list|(
literal|"/c"
argument_list|,
literal|"/s"
argument_list|,
literal|"/foo%2Fbar///"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"/foo%2Fbar/"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|emptyPathInfo ()
specifier|public
name|void
name|emptyPathInfo
parameter_list|()
block|{
name|assertThat
argument_list|(
name|RequestUtil
operator|.
name|getEncodedPathInfo
argument_list|(
name|fakeRequest
argument_list|(
literal|"/c"
argument_list|,
literal|"/s"
argument_list|,
literal|""
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
DECL|method|fakeRequest ( String contextPath, String servletPath, String pathInfo)
specifier|private
name|FakeHttpServletRequest
name|fakeRequest
parameter_list|(
name|String
name|contextPath
parameter_list|,
name|String
name|servletPath
parameter_list|,
name|String
name|pathInfo
parameter_list|)
block|{
name|FakeHttpServletRequest
name|req
init|=
operator|new
name|FakeHttpServletRequest
argument_list|(
literal|"gerrit.example.com"
argument_list|,
literal|80
argument_list|,
name|contextPath
argument_list|,
name|servletPath
argument_list|)
decl_stmt|;
return|return
name|req
operator|.
name|setPathInfo
argument_list|(
name|pathInfo
argument_list|)
return|;
block|}
block|}
end_class

end_unit

