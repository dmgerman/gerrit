begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|truth
operator|.
name|ConfigSubject
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|nio
operator|.
name|file
operator|.
name|Files
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
name|Test
import|;
end_import

begin_class
DECL|class|MergeableFileBasedConfigTest
specifier|public
class|class
name|MergeableFileBasedConfigTest
block|{
annotation|@
name|Test
DECL|method|mergeNull ()
specifier|public
name|void
name|mergeNull
parameter_list|()
throws|throws
name|Exception
block|{
name|MergeableFileBasedConfig
name|cfg
init|=
name|newConfig
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"[foo]\n\tbar = value\n"
decl_stmt|;
name|assertConfig
argument_list|(
name|cfg
argument_list|,
name|expected
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|merge
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertConfig
argument_list|(
name|cfg
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|mergeFlatConfig ()
specifier|public
name|void
name|mergeFlatConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|MergeableFileBasedConfig
name|cfg
init|=
name|newConfig
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"foo"
argument_list|,
literal|"sub"
argument_list|,
literal|"bar1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"foo"
argument_list|,
literal|"sub"
argument_list|,
literal|"bar2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|assertConfig
argument_list|(
name|cfg
argument_list|,
literal|"[foo]\n"
operator|+
literal|"\tbar1 = value1\n"
operator|+
literal|"\tbar2 = value2\n"
operator|+
literal|"[foo \"sub\"]\n"
operator|+
literal|"\tbar1 = value1\n"
operator|+
literal|"\tbar2 = value2\n"
argument_list|)
expr_stmt|;
name|Config
name|toMerge
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|toMerge
operator|.
name|setStringList
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar2"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"merge1"
argument_list|,
literal|"merge2"
argument_list|)
argument_list|)
expr_stmt|;
name|toMerge
operator|.
name|setStringList
argument_list|(
literal|"foo"
argument_list|,
literal|"sub"
argument_list|,
literal|"bar2"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"merge1"
argument_list|,
literal|"merge2"
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|merge
argument_list|(
name|toMerge
argument_list|)
expr_stmt|;
name|assertConfig
argument_list|(
name|cfg
argument_list|,
literal|"[foo]\n"
operator|+
literal|"\tbar1 = value1\n"
operator|+
literal|"\tbar2 = merge1\n"
operator|+
literal|"\tbar2 = merge2\n"
operator|+
literal|"[foo \"sub\"]\n"
operator|+
literal|"\tbar1 = value1\n"
operator|+
literal|"\tbar2 = merge1\n"
operator|+
literal|"\tbar2 = merge2\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|mergeStackedConfig ()
specifier|public
name|void
name|mergeStackedConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|MergeableFileBasedConfig
name|cfg
init|=
name|newConfig
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"foo"
argument_list|,
literal|"sub"
argument_list|,
literal|"bar1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"foo"
argument_list|,
literal|"sub"
argument_list|,
literal|"bar2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|assertConfig
argument_list|(
name|cfg
argument_list|,
literal|"[foo]\n"
operator|+
literal|"\tbar1 = value1\n"
operator|+
literal|"\tbar2 = value2\n"
operator|+
literal|"[foo \"sub\"]\n"
operator|+
literal|"\tbar1 = value1\n"
operator|+
literal|"\tbar2 = value2\n"
argument_list|)
expr_stmt|;
name|Config
name|base
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|Config
name|toMerge
init|=
operator|new
name|Config
argument_list|(
name|base
argument_list|)
decl_stmt|;
name|base
operator|.
name|setStringList
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
literal|"bar2"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"merge1"
argument_list|,
literal|"merge2"
argument_list|)
argument_list|)
expr_stmt|;
name|base
operator|.
name|setStringList
argument_list|(
literal|"foo"
argument_list|,
literal|"sub"
argument_list|,
literal|"bar2"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"merge1"
argument_list|,
literal|"merge2"
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|merge
argument_list|(
name|toMerge
argument_list|)
expr_stmt|;
name|assertConfig
argument_list|(
name|cfg
argument_list|,
literal|"[foo]\n"
operator|+
literal|"\tbar1 = value1\n"
operator|+
literal|"\tbar2 = merge1\n"
operator|+
literal|"\tbar2 = merge2\n"
operator|+
literal|"[foo \"sub\"]\n"
operator|+
literal|"\tbar1 = value1\n"
operator|+
literal|"\tbar2 = merge1\n"
operator|+
literal|"\tbar2 = merge2\n"
argument_list|)
expr_stmt|;
block|}
DECL|method|newConfig ()
specifier|private
name|MergeableFileBasedConfig
name|newConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|f
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
literal|".config"
argument_list|)
decl_stmt|;
name|f
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
return|return
operator|new
name|MergeableFileBasedConfig
argument_list|(
name|f
argument_list|,
name|FS
operator|.
name|detect
argument_list|()
argument_list|)
return|;
block|}
DECL|method|assertConfig (MergeableFileBasedConfig cfg, String expected)
specifier|private
name|void
name|assertConfig
parameter_list|(
name|MergeableFileBasedConfig
name|cfg
parameter_list|,
name|String
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|cfg
argument_list|)
operator|.
name|text
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|save
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|cfg
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

