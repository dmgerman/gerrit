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

begin_comment
comment|//
end_comment

begin_comment
comment|// Portions related to finding the hook script to execute are:
end_comment

begin_comment
comment|// Copyright (C) 2008, Imran M Yousuf<imyousuf@smartitengineering.com>
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// All rights reserved.
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Redistribution and use in source and binary forms, with or
end_comment

begin_comment
comment|// without modification, are permitted provided that the following
end_comment

begin_comment
comment|// conditions are met:
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// - Redistributions of source code must retain the above copyright
end_comment

begin_comment
comment|// notice, this list of conditions and the following disclaimer.
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// - Redistributions in binary form must reproduce the above
end_comment

begin_comment
comment|// copyright notice, this list of conditions and the following
end_comment

begin_comment
comment|// disclaimer in the documentation and/or other materials provided
end_comment

begin_comment
comment|// with the distribution.
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// - Neither the name of the Git Development Community nor the
end_comment

begin_comment
comment|// names of its contributors may be used to endorse or promote
end_comment

begin_comment
comment|// products derived from this software without specific prior
end_comment

begin_comment
comment|// written permission.
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
end_comment

begin_comment
comment|// CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
end_comment

begin_comment
comment|// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
end_comment

begin_comment
comment|// OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
end_comment

begin_comment
comment|// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
end_comment

begin_comment
comment|// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
end_comment

begin_comment
comment|// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
end_comment

begin_comment
comment|// NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
end_comment

begin_comment
comment|// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
end_comment

begin_comment
comment|// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
end_comment

begin_comment
comment|// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
end_comment

begin_comment
comment|// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
end_comment

begin_comment
comment|// ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
end_comment

begin_package
DECL|package|com.google.gerrit.server.ssh.scproot.hooks
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
operator|.
name|scproot
operator|.
name|hooks
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
name|testutil
operator|.
name|LocalDiskRepositoryTestCase
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
name|Repository
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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_class
DECL|class|HookTestCase
specifier|public
specifier|abstract
class|class
name|HookTestCase
extends|extends
name|LocalDiskRepositoryTestCase
block|{
DECL|field|repository
specifier|protected
name|Repository
name|repository
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
name|repository
operator|=
name|createWorkRepository
argument_list|()
expr_stmt|;
block|}
DECL|method|getHook (final String name)
specifier|protected
name|File
name|getHook
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
specifier|final
name|String
name|scproot
init|=
literal|"com/google/gerrit/server/ssh/scproot"
decl_stmt|;
specifier|final
name|String
name|path
init|=
name|scproot
operator|+
literal|"/hooks/"
operator|+
name|name
decl_stmt|;
specifier|final
name|URL
name|url
init|=
name|cl
argument_list|()
operator|.
name|getResource
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Cannot locate "
operator|+
name|path
operator|+
literal|" in CLASSPATH"
argument_list|)
expr_stmt|;
block|}
name|File
name|hook
decl_stmt|;
try|try
block|{
name|hook
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|hook
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hook
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Cannot locate "
operator|+
name|path
operator|+
literal|" in CLASSPATH"
argument_list|)
expr_stmt|;
block|}
comment|// The hook was copied out of our source control system into the
comment|// target area by Java tools. Its not executable in the source
comment|// are, nor did the copying Java program make it executable in the
comment|// destination area. So we must force it to be executable.
comment|//
specifier|final
name|long
name|time
init|=
name|hook
operator|.
name|lastModified
argument_list|()
decl_stmt|;
name|hook
operator|.
name|setExecutable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|hook
operator|.
name|setLastModified
argument_list|(
name|time
argument_list|)
expr_stmt|;
return|return
name|hook
return|;
block|}
DECL|method|cl ()
specifier|private
name|ClassLoader
name|cl
parameter_list|()
block|{
return|return
name|HookTestCase
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
block|}
end_class

end_unit

