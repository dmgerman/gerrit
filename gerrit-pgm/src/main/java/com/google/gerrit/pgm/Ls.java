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
name|main
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
name|pgm
operator|.
name|util
operator|.
name|AbstractProgram
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
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipFile
import|;
end_import

begin_comment
comment|/** List the files available in our archive. */
end_comment

begin_class
DECL|class|Ls
specifier|public
class|class
name|Ls
extends|extends
name|AbstractProgram
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|int
name|run
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|ZipFile
name|zf
init|=
operator|new
name|ZipFile
argument_list|(
name|GerritLauncher
operator|.
name|getDistributionArchive
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|Enumeration
argument_list|<
name|?
extends|extends
name|ZipEntry
argument_list|>
name|e
init|=
name|zf
operator|.
name|entries
argument_list|()
decl_stmt|;
while|while
condition|(
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
specifier|final
name|ZipEntry
name|ze
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|ze
operator|.
name|getName
argument_list|()
decl_stmt|;
name|boolean
name|show
init|=
literal|false
decl_stmt|;
name|show
operator||=
name|name
operator|.
name|startsWith
argument_list|(
literal|"WEB-INF/"
argument_list|)
expr_stmt|;
name|show
operator||=
name|name
operator|.
name|equals
argument_list|(
literal|"LICENSES.txt"
argument_list|)
expr_stmt|;
name|show
operator|&=
operator|!
name|ze
operator|.
name|isDirectory
argument_list|()
expr_stmt|;
name|show
operator|&=
operator|!
name|name
operator|.
name|startsWith
argument_list|(
literal|"WEB-INF/classes/"
argument_list|)
expr_stmt|;
name|show
operator|&=
operator|!
name|name
operator|.
name|startsWith
argument_list|(
literal|"WEB-INF/lib/"
argument_list|)
expr_stmt|;
name|show
operator|&=
operator|!
name|name
operator|.
name|equals
argument_list|(
literal|"WEB-INF/web.xml"
argument_list|)
expr_stmt|;
if|if
condition|(
name|show
condition|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"WEB-INF/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|"WEB-INF/"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|zf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

