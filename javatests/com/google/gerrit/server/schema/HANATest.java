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

begin_package
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|testing
operator|.
name|GerritBaseTests
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
DECL|class|HANATest
specifier|public
class|class
name|HANATest
extends|extends
name|GerritBaseTests
block|{
DECL|field|hana
specifier|private
name|HANA
name|hana
decl_stmt|;
DECL|field|config
specifier|private
name|Config
name|config
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|config
operator|=
operator|new
name|Config
argument_list|()
expr_stmt|;
name|config
operator|.
name|setString
argument_list|(
literal|"database"
argument_list|,
literal|null
argument_list|,
literal|"hostname"
argument_list|,
literal|"my.host"
argument_list|)
expr_stmt|;
name|hana
operator|=
operator|new
name|HANA
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getUrl ()
specifier|public
name|void
name|getUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|config
operator|.
name|setString
argument_list|(
literal|"database"
argument_list|,
literal|null
argument_list|,
literal|"port"
argument_list|,
literal|"4242"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|hana
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"jdbc:sap://my.host:4242"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getIndexScript ()
specifier|public
name|void
name|getIndexScript
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|hana
operator|.
name|getIndexScript
argument_list|()
argument_list|)
operator|.
name|isSameAs
argument_list|(
name|ScriptRunner
operator|.
name|NOOP
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

