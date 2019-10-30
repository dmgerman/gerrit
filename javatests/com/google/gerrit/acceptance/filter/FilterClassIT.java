begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.filter
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|filter
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
name|testing
operator|.
name|ConfigSuite
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
name|Assert
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
DECL|class|FilterClassIT
specifier|public
class|class
name|FilterClassIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Default
DECL|method|enableFilter ()
specifier|public
specifier|static
name|Config
name|enableFilter
parameter_list|()
throws|throws
name|ConfigInvalidException
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
name|fromText
argument_list|(
literal|""
operator|+
literal|"[httpd]\n"
operator|+
literal|"    filterClass = com.google.gerrit.acceptance.filter.FakeNoInitParamsFilter\n"
operator|+
literal|"    filterClass = com.google.gerrit.acceptance.filter.FakeMustInitParamsFilter\n"
operator|+
literal|"[filterClass \"com.google.gerrit.acceptance.filter.FakeMustInitParamsFilter\"]\n"
operator|+
literal|"    PARAM-1 = hello\n"
operator|+
literal|"    PARAM-2 = world\n"
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|Test
DECL|method|filterLoad ()
specifier|public
name|void
name|filterLoad
parameter_list|()
block|{
name|FakeNoInitParamsFilter
name|fakeNoInitParamsFilter
init|=
name|server
operator|.
name|getTestInjector
argument_list|()
operator|.
name|getBinding
argument_list|(
name|FakeNoInitParamsFilter
operator|.
name|class
argument_list|)
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|fakeNoInitParamsFilter
argument_list|)
expr_stmt|;
name|FakeMustInitParamsFilter
name|fakeMustInitParamsFilter
init|=
name|server
operator|.
name|getTestInjector
argument_list|()
operator|.
name|getBinding
argument_list|(
name|FakeMustInitParamsFilter
operator|.
name|class
argument_list|)
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|fakeMustInitParamsFilter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|filterInitParams ()
specifier|public
name|void
name|filterInitParams
parameter_list|()
block|{
name|FakeMustInitParamsFilter
name|fakeMustInitParamsFilter
init|=
name|server
operator|.
name|getTestInjector
argument_list|()
operator|.
name|getBinding
argument_list|(
name|FakeMustInitParamsFilter
operator|.
name|class
argument_list|)
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|fakeMustInitParamsFilter
operator|.
name|getInitParams
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|fakeMustInitParamsFilter
operator|.
name|getInitParams
argument_list|()
operator|.
name|get
argument_list|(
literal|"PARAM-1"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"world"
argument_list|,
name|fakeMustInitParamsFilter
operator|.
name|getInitParams
argument_list|()
operator|.
name|get
argument_list|(
literal|"PARAM-2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

