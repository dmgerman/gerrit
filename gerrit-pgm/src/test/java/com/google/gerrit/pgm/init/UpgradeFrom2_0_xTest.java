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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createStrictMock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
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
name|pgm
operator|.
name|util
operator|.
name|ConsoleUI
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
name|SitePaths
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
name|errors
operator|.
name|ConfigInvalidException
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
name|storage
operator|.
name|file
operator|.
name|FileBasedConfig
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
name|util
operator|.
name|FS
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
name|util
operator|.
name|IO
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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
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
name|io
operator|.
name|Writer
import|;
end_import

begin_class
DECL|class|UpgradeFrom2_0_xTest
specifier|public
class|class
name|UpgradeFrom2_0_xTest
extends|extends
name|InitTestCase
block|{
annotation|@
name|Test
DECL|method|testUpgrade ()
specifier|public
name|void
name|testUpgrade
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
specifier|final
name|File
name|p
init|=
name|newSitePath
argument_list|()
decl_stmt|;
specifier|final
name|SitePaths
name|site
init|=
operator|new
name|SitePaths
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|site
operator|.
name|isNew
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|site
operator|.
name|site_path
operator|.
name|mkdir
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|site
operator|.
name|etc_dir
operator|.
name|mkdir
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|n
range|:
name|UpgradeFrom2_0_x
operator|.
name|etcFiles
control|)
block|{
name|Writer
name|w
init|=
operator|new
name|FileWriter
argument_list|(
operator|new
name|File
argument_list|(
name|p
argument_list|,
name|n
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|w
operator|.
name|write
argument_list|(
literal|"# "
operator|+
name|n
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|w
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|FileBasedConfig
name|old
init|=
operator|new
name|FileBasedConfig
argument_list|(
operator|new
name|File
argument_list|(
name|p
argument_list|,
literal|"gerrit.config"
argument_list|)
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
name|old
operator|.
name|setString
argument_list|(
literal|"ldap"
argument_list|,
literal|null
argument_list|,
literal|"username"
argument_list|,
literal|"ldap.user"
argument_list|)
expr_stmt|;
name|old
operator|.
name|setString
argument_list|(
literal|"ldap"
argument_list|,
literal|null
argument_list|,
literal|"password"
argument_list|,
literal|"ldap.s3kr3t"
argument_list|)
expr_stmt|;
name|old
operator|.
name|setString
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"smtpUser"
argument_list|,
literal|"email.user"
argument_list|)
expr_stmt|;
name|old
operator|.
name|setString
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"smtpPass"
argument_list|,
literal|"email.s3kr3t"
argument_list|)
expr_stmt|;
name|old
operator|.
name|save
argument_list|()
expr_stmt|;
specifier|final
name|InitFlags
name|flags
init|=
operator|new
name|InitFlags
argument_list|(
name|site
argument_list|)
decl_stmt|;
specifier|final
name|ConsoleUI
name|ui
init|=
name|createStrictMock
argument_list|(
name|ConsoleUI
operator|.
name|class
argument_list|)
decl_stmt|;
name|Section
operator|.
name|Factory
name|sections
init|=
operator|new
name|Section
operator|.
name|Factory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Section
name|get
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|subsection
parameter_list|)
block|{
return|return
operator|new
name|Section
argument_list|(
name|flags
argument_list|,
name|site
argument_list|,
name|ui
argument_list|,
name|name
argument_list|,
name|subsection
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|expect
argument_list|(
name|ui
operator|.
name|yesno
argument_list|(
name|eq
argument_list|(
literal|true
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"Upgrade '%s'"
argument_list|)
argument_list|,
name|eq
argument_list|(
name|p
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|ui
argument_list|)
expr_stmt|;
name|UpgradeFrom2_0_x
name|u
init|=
operator|new
name|UpgradeFrom2_0_x
argument_list|(
name|site
argument_list|,
name|flags
argument_list|,
name|ui
argument_list|,
name|sections
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|u
operator|.
name|isNeedUpgrade
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|u
operator|.
name|isNeedUpgrade
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|ui
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|n
range|:
name|UpgradeFrom2_0_x
operator|.
name|etcFiles
control|)
block|{
if|if
condition|(
literal|"gerrit.config"
operator|.
name|equals
argument_list|(
name|n
argument_list|)
condition|)
continue|continue;
if|if
condition|(
literal|"secure.config"
operator|.
name|equals
argument_list|(
name|n
argument_list|)
condition|)
continue|continue;
name|assertEquals
argument_list|(
literal|"# "
operator|+
name|n
operator|+
literal|"\n"
argument_list|,
comment|//
operator|new
name|String
argument_list|(
name|IO
operator|.
name|readFully
argument_list|(
operator|new
name|File
argument_list|(
name|site
operator|.
name|etc_dir
argument_list|,
name|n
argument_list|)
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|FileBasedConfig
name|cfg
init|=
operator|new
name|FileBasedConfig
argument_list|(
name|site
operator|.
name|gerrit_config
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
name|FileBasedConfig
name|sec
init|=
operator|new
name|FileBasedConfig
argument_list|(
name|site
operator|.
name|secure_config
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|load
argument_list|()
expr_stmt|;
name|sec
operator|.
name|load
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"email.user"
argument_list|,
name|cfg
operator|.
name|getString
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"smtpUser"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"smtpPass"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"email.s3kr3t"
argument_list|,
name|sec
operator|.
name|getString
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"smtpPass"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ldap.user"
argument_list|,
name|cfg
operator|.
name|getString
argument_list|(
literal|"ldap"
argument_list|,
literal|null
argument_list|,
literal|"username"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"ldap"
argument_list|,
literal|null
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ldap.s3kr3t"
argument_list|,
name|sec
operator|.
name|getString
argument_list|(
literal|"ldap"
argument_list|,
literal|null
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|u
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

