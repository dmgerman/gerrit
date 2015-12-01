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
DECL|package|com.google.gerrit.httpd.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|plugins
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
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_class
DECL|class|LfsPluginServletTest
specifier|public
class|class
name|LfsPluginServletTest
block|{
annotation|@
name|Test
DECL|method|noLfsEndPoint_noMatch ()
specifier|public
name|void
name|noLfsEndPoint_noMatch
parameter_list|()
block|{
name|Pattern
name|p
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|LfsPluginServlet
operator|.
name|URL_REGEX
argument_list|)
decl_stmt|;
name|doesNotMatch
argument_list|(
name|p
argument_list|,
literal|"/foo"
argument_list|)
expr_stmt|;
name|doesNotMatch
argument_list|(
name|p
argument_list|,
literal|"/a/foo"
argument_list|)
expr_stmt|;
name|doesNotMatch
argument_list|(
name|p
argument_list|,
literal|"/p/foo"
argument_list|)
expr_stmt|;
name|doesNotMatch
argument_list|(
name|p
argument_list|,
literal|"/a/p/foo"
argument_list|)
expr_stmt|;
name|doesNotMatch
argument_list|(
name|p
argument_list|,
literal|"/info/lfs/objects/batch"
argument_list|)
expr_stmt|;
name|doesNotMatch
argument_list|(
name|p
argument_list|,
literal|"/info/lfs/objects/batch/foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|matchingLfsEndpoint_projectNameCaptured ()
specifier|public
name|void
name|matchingLfsEndpoint_projectNameCaptured
parameter_list|()
block|{
name|Pattern
name|p
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|LfsPluginServlet
operator|.
name|URL_REGEX
argument_list|)
decl_stmt|;
name|matches
argument_list|(
name|p
argument_list|,
literal|"/foo/bar/info/lfs/objects/batch"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|matches
argument_list|(
name|p
argument_list|,
literal|"/a/foo/bar/info/lfs/objects/batch"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|matches
argument_list|(
name|p
argument_list|,
literal|"/p/foo/bar/info/lfs/objects/batch"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|matches
argument_list|(
name|p
argument_list|,
literal|"/a/p/foo/bar/info/lfs/objects/batch"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|doesNotMatch (Pattern p, String input)
specifier|private
name|void
name|doesNotMatch
parameter_list|(
name|Pattern
name|p
parameter_list|,
name|String
name|input
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|p
operator|.
name|matcher
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|m
operator|.
name|matches
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
DECL|method|matches (Pattern p, String input, String expectedProjectName)
specifier|private
name|void
name|matches
parameter_list|(
name|Pattern
name|p
parameter_list|,
name|String
name|input
parameter_list|,
name|String
name|expectedProjectName
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|p
operator|.
name|matcher
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|m
operator|.
name|matches
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedProjectName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

