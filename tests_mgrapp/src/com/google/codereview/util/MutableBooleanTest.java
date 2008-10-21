begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.codereview.util
package|package
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|util
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
DECL|class|MutableBooleanTest
specifier|public
class|class
name|MutableBooleanTest
extends|extends
name|TestCase
block|{
DECL|method|testCreateDefault ()
specifier|public
name|void
name|testCreateDefault
parameter_list|()
block|{
specifier|final
name|MutableBoolean
name|b
init|=
operator|new
name|MutableBoolean
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|b
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateTrue ()
specifier|public
name|void
name|testCreateTrue
parameter_list|()
block|{
specifier|final
name|MutableBoolean
name|b
init|=
operator|new
name|MutableBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|b
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFalse ()
specifier|public
name|void
name|testCreateFalse
parameter_list|()
block|{
specifier|final
name|MutableBoolean
name|b
init|=
operator|new
name|MutableBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|b
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testMutable ()
specifier|public
name|void
name|testMutable
parameter_list|()
block|{
specifier|final
name|MutableBoolean
name|b
init|=
operator|new
name|MutableBoolean
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|b
operator|.
name|value
argument_list|)
expr_stmt|;
name|b
operator|.
name|value
operator|=
literal|true
expr_stmt|;
name|assertTrue
argument_list|(
name|b
operator|.
name|value
argument_list|)
expr_stmt|;
name|b
operator|.
name|value
operator|=
literal|false
expr_stmt|;
name|assertFalse
argument_list|(
name|b
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

