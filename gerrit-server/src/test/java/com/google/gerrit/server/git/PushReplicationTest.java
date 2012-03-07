begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|PushReplication
operator|.
name|ReplicationConfig
operator|.
name|encode
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|PushReplication
operator|.
name|ReplicationConfig
operator|.
name|needsUrlEncoding
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|URIish
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_class
DECL|class|PushReplicationTest
specifier|public
class|class
name|PushReplicationTest
extends|extends
name|TestCase
block|{
DECL|method|testNeedsUrlEncoding ()
specifier|public
name|void
name|testNeedsUrlEncoding
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|assertTrue
argument_list|(
name|needsUrlEncoding
argument_list|(
operator|new
name|URIish
argument_list|(
literal|"http://host/path"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|needsUrlEncoding
argument_list|(
operator|new
name|URIish
argument_list|(
literal|"https://host/path"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|needsUrlEncoding
argument_list|(
operator|new
name|URIish
argument_list|(
literal|"amazon-s3://config/bucket/path"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|needsUrlEncoding
argument_list|(
operator|new
name|URIish
argument_list|(
literal|"host:path"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|needsUrlEncoding
argument_list|(
operator|new
name|URIish
argument_list|(
literal|"user@host:path"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|needsUrlEncoding
argument_list|(
operator|new
name|URIish
argument_list|(
literal|"git://host/path"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|needsUrlEncoding
argument_list|(
operator|new
name|URIish
argument_list|(
literal|"ssh://host/path"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUrlEncoding ()
specifier|public
name|void
name|testUrlEncoding
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo/bar/thing"
argument_list|,
name|encode
argument_list|(
literal|"foo/bar/thing"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"--%20All%20Projects%20--"
argument_list|,
name|encode
argument_list|(
literal|"-- All Projects --"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name/with%20a%20space"
argument_list|,
name|encode
argument_list|(
literal|"name/with a space"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name%0Awith-LF"
argument_list|,
name|encode
argument_list|(
literal|"name\nwith-LF"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

