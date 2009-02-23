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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
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
name|client
operator|.
name|rpc
operator|.
name|Common
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
name|git
operator|.
name|WorkQueue
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
name|GerritServer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|XsrfException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_comment
comment|/**  * Creates the Gerrit 2 database schema.  */
end_comment

begin_class
DECL|class|CreateSchema
specifier|public
class|class
name|CreateSchema
block|{
DECL|method|main (final String[] argv)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|argv
parameter_list|)
throws|throws
name|OrmException
throws|,
name|XsrfException
block|{
try|try
block|{
name|mainImpl
argument_list|(
name|argv
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|WorkQueue
operator|.
name|terminate
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|mainImpl (final String[] argv)
specifier|private
specifier|static
name|void
name|mainImpl
parameter_list|(
specifier|final
name|String
index|[]
name|argv
parameter_list|)
throws|throws
name|OrmException
throws|,
name|XsrfException
block|{
name|GerritServer
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|Common
operator|.
name|getSchemaFactory
argument_list|()
operator|.
name|open
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Gerrit2 schema initialized"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

