begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|util
operator|.
name|HostPlatform
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|NotDirectoryException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_class
DECL|class|SitePathsTest
specifier|public
class|class
name|SitePathsTest
block|{
annotation|@
name|Rule
DECL|field|exception
specifier|public
name|ExpectedException
name|exception
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testCreate_NotExisting ()
specifier|public
name|void
name|testCreate_NotExisting
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Path
name|root
init|=
name|random
argument_list|()
decl_stmt|;
specifier|final
name|SitePaths
name|site
init|=
operator|new
name|SitePaths
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|site
operator|.
name|isNew
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|root
argument_list|,
name|site
operator|.
name|site_path
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|root
operator|.
name|resolve
argument_list|(
literal|"etc"
argument_list|)
argument_list|,
name|site
operator|.
name|etc_dir
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreate_Empty ()
specifier|public
name|void
name|testCreate_Empty
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Path
name|root
init|=
name|random
argument_list|()
decl_stmt|;
try|try
block|{
name|Files
operator|.
name|createDirectory
argument_list|(
name|root
argument_list|)
expr_stmt|;
specifier|final
name|SitePaths
name|site
init|=
operator|new
name|SitePaths
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|site
operator|.
name|isNew
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|root
argument_list|,
name|site
operator|.
name|site_path
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Files
operator|.
name|delete
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testCreate_NonEmpty ()
specifier|public
name|void
name|testCreate_NonEmpty
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Path
name|root
init|=
name|random
argument_list|()
decl_stmt|;
specifier|final
name|Path
name|txt
init|=
name|root
operator|.
name|resolve
argument_list|(
literal|"test.txt"
argument_list|)
decl_stmt|;
try|try
block|{
name|Files
operator|.
name|createDirectory
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createFile
argument_list|(
name|txt
argument_list|)
expr_stmt|;
specifier|final
name|SitePaths
name|site
init|=
operator|new
name|SitePaths
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|site
operator|.
name|isNew
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|root
argument_list|,
name|site
operator|.
name|site_path
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Files
operator|.
name|delete
argument_list|(
name|txt
argument_list|)
expr_stmt|;
name|Files
operator|.
name|delete
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testCreate_NotDirectory ()
specifier|public
name|void
name|testCreate_NotDirectory
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Path
name|root
init|=
name|random
argument_list|()
decl_stmt|;
try|try
block|{
name|Files
operator|.
name|createFile
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|NotDirectoryException
operator|.
name|class
argument_list|)
expr_stmt|;
operator|new
name|SitePaths
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Files
operator|.
name|delete
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testResolve ()
specifier|public
name|void
name|testResolve
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Path
name|root
init|=
name|random
argument_list|()
decl_stmt|;
specifier|final
name|SitePaths
name|site
init|=
operator|new
name|SitePaths
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|site
operator|.
name|resolve
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|site
operator|.
name|resolve
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|site
operator|.
name|resolve
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|root
operator|.
name|resolve
argument_list|(
literal|"a"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|normalize
argument_list|()
argument_list|,
name|site
operator|.
name|resolve
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|String
name|pfx
init|=
name|HostPlatform
operator|.
name|isWin32
argument_list|()
condition|?
literal|"C:/"
else|:
literal|"/"
decl_stmt|;
name|assertNotNull
argument_list|(
name|site
operator|.
name|resolve
argument_list|(
name|pfx
operator|+
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|pfx
operator|+
literal|"a"
argument_list|)
argument_list|,
name|site
operator|.
name|resolve
argument_list|(
name|pfx
operator|+
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|random ()
specifier|private
specifier|static
name|Path
name|random
parameter_list|()
throws|throws
name|IOException
block|{
name|Path
name|tmp
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
literal|"gerrit_test_"
argument_list|,
literal|"_site"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
return|return
name|tmp
return|;
block|}
block|}
end_class

end_unit

