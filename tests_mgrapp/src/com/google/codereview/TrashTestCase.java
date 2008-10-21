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
DECL|package|com.google.codereview
package|package
name|com
operator|.
name|google
operator|.
name|codereview
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * JUnit TestCase with support for creating a temporary directory.  */
end_comment

begin_class
DECL|class|TrashTestCase
specifier|public
specifier|abstract
class|class
name|TrashTestCase
extends|extends
name|TestCase
block|{
DECL|field|tempRoot
specifier|protected
name|File
name|tempRoot
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|tempRoot
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"codereview"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|tempRoot
operator|.
name|delete
argument_list|()
expr_stmt|;
name|tempRoot
operator|.
name|mkdir
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|tempRoot
operator|!=
literal|null
condition|)
block|{
name|rm
argument_list|(
name|tempRoot
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|rm (final File dir)
specifier|protected
specifier|static
name|void
name|rm
parameter_list|(
specifier|final
name|File
name|dir
parameter_list|)
block|{
specifier|final
name|File
index|[]
name|list
init|=
name|dir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|list
operator|!=
literal|null
operator|&&
name|i
operator|<
name|list
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|File
name|f
init|=
name|list
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"."
argument_list|)
operator|||
name|f
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|".."
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|rm
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
name|dir
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

