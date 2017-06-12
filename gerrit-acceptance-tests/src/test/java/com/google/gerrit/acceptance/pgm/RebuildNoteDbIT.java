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
DECL|package|com.google.gerrit.acceptance.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|pgm
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
name|launcher
operator|.
name|GerritLauncher
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|ConfigNotesMigration
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
name|notedb
operator|.
name|NotesMigrationState
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
name|TempFileUtil
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
name|StoredConfig
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
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|RebuildNoteDbIT
specifier|public
class|class
name|RebuildNoteDbIT
block|{
DECL|field|sitePath
specifier|private
name|String
name|sitePath
decl_stmt|;
DECL|field|gerritConfig
specifier|private
name|StoredConfig
name|gerritConfig
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|SitePaths
name|sitePaths
init|=
operator|new
name|SitePaths
argument_list|(
name|TempFileUtil
operator|.
name|createTempDirectory
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
name|sitePath
operator|=
name|sitePaths
operator|.
name|site_path
operator|.
name|toString
argument_list|()
expr_stmt|;
name|gerritConfig
operator|=
operator|new
name|FileBasedConfig
argument_list|(
name|sitePaths
operator|.
name|gerrit_config
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|detect
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|TempFileUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|rebuildEmptySiteStartingWithNoteDbEnabled ()
specifier|public
name|void
name|rebuildEmptySiteStartingWithNoteDbEnabled
parameter_list|()
throws|throws
name|Exception
block|{
name|initSite
argument_list|()
expr_stmt|;
name|setNotesMigrationState
argument_list|(
name|NotesMigrationState
operator|.
name|NOTE_DB_UNFUSED
argument_list|)
expr_stmt|;
name|runGerrit
argument_list|(
literal|"RebuildNoteDb"
argument_list|,
literal|"-d"
argument_list|,
name|sitePath
argument_list|,
literal|"--show-stack-trace"
argument_list|)
expr_stmt|;
block|}
DECL|method|initSite ()
specifier|private
name|void
name|initSite
parameter_list|()
throws|throws
name|Exception
block|{
name|runGerrit
argument_list|(
literal|"init"
argument_list|,
literal|"-d"
argument_list|,
name|sitePath
argument_list|,
literal|"--batch"
argument_list|,
literal|"--no-auto-start"
argument_list|,
literal|"--skip-plugins"
argument_list|,
literal|"--show-stack-trace"
argument_list|)
expr_stmt|;
block|}
DECL|method|runGerrit (String... args)
specifier|private
specifier|static
name|void
name|runGerrit
parameter_list|(
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|GerritLauncher
operator|.
name|mainImpl
argument_list|(
name|args
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|setNotesMigrationState (NotesMigrationState state)
specifier|private
name|void
name|setNotesMigrationState
parameter_list|(
name|NotesMigrationState
name|state
parameter_list|)
throws|throws
name|Exception
block|{
name|gerritConfig
operator|.
name|load
argument_list|()
expr_stmt|;
name|ConfigNotesMigration
operator|.
name|setConfigValues
argument_list|(
name|gerritConfig
argument_list|,
name|state
operator|.
name|migration
argument_list|()
argument_list|)
expr_stmt|;
name|gerritConfig
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

