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
DECL|package|com.google.gerrit.httpd.raw
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|raw
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
name|entities
operator|.
name|Change
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
name|entities
operator|.
name|Patch
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
name|entities
operator|.
name|PatchSet
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
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceConflictException
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
name|extensions
operator|.
name|restapi
operator|.
name|Url
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
name|PatchSetUtil
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
name|edit
operator|.
name|ChangeEdit
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
name|edit
operator|.
name|ChangeEditUtil
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
name|ChangeNotes
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
name|permissions
operator|.
name|ChangePermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|project
operator|.
name|NoSuchChangeException
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
name|project
operator|.
name|ProjectCache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|Optional
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|ObjectId
import|;
end_import

begin_comment
comment|/**  * Exports a single version of a patch as a normal file download.  *  *<p>This can be relatively unsafe with Microsoft Internet Explorer 6.0 as the browser will (rather  * incorrectly) treat an HTML or JavaScript file its supposed to download as though it was served by  * this site, and will execute it with the site's own protection domain. This opens a massive  * security hole so we package the content into a zip file.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|CatServlet
specifier|public
class|class
name|CatServlet
extends|extends
name|HttpServlet
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|changeEditUtil
specifier|private
specifier|final
name|ChangeEditUtil
name|changeEditUtil
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|changeNotesFactory
specifier|private
specifier|final
name|ChangeNotes
operator|.
name|Factory
name|changeNotesFactory
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|CatServlet ( ChangeEditUtil ceu, PatchSetUtil psu, ChangeNotes.Factory cnf, PermissionBackend pb, ProjectCache pc)
name|CatServlet
parameter_list|(
name|ChangeEditUtil
name|ceu
parameter_list|,
name|PatchSetUtil
name|psu
parameter_list|,
name|ChangeNotes
operator|.
name|Factory
name|cnf
parameter_list|,
name|PermissionBackend
name|pb
parameter_list|,
name|ProjectCache
name|pc
parameter_list|)
block|{
name|changeEditUtil
operator|=
name|ceu
expr_stmt|;
name|psUtil
operator|=
name|psu
expr_stmt|;
name|changeNotesFactory
operator|=
name|cnf
expr_stmt|;
name|permissionBackend
operator|=
name|pb
expr_stmt|;
name|projectCache
operator|=
name|pc
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doGet (HttpServletRequest req, HttpServletResponse rsp)
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|keyStr
init|=
name|req
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
comment|// We shouldn't have to do this extra decode pass, but somehow we
comment|// are now receiving our "^1" suffix as "%5E1", which confuses us
comment|// downstream. Other times we get our embedded "," as "%2C", which
comment|// is equally bad. And yet when these happen a "%2F" is left as-is,
comment|// rather than escaped as "%252F", which makes me feel really really
comment|// uncomfortable with a blind decode right here.
comment|//
name|keyStr
operator|=
name|Url
operator|.
name|decode
argument_list|(
name|keyStr
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|keyStr
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
name|keyStr
operator|=
name|keyStr
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|Patch
operator|.
name|Key
name|patchKey
decl_stmt|;
specifier|final
name|int
name|side
decl_stmt|;
block|{
specifier|final
name|int
name|c
init|=
name|keyStr
operator|.
name|lastIndexOf
argument_list|(
literal|'^'
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
name|side
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|side
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|keyStr
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|keyStr
operator|=
name|keyStr
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
try|try
block|{
name|patchKey
operator|=
name|Patch
operator|.
name|Key
operator|.
name|parse
argument_list|(
name|keyStr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchKey
operator|.
name|patchSetId
argument_list|()
operator|.
name|changeId
argument_list|()
decl_stmt|;
name|String
name|revision
decl_stmt|;
try|try
block|{
name|ChangeNotes
name|notes
init|=
name|changeNotesFactory
operator|.
name|createChecked
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|change
argument_list|(
name|notes
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|)
operator|.
name|checkStatePermitsRead
argument_list|()
expr_stmt|;
if|if
condition|(
name|patchKey
operator|.
name|patchSetId
argument_list|()
operator|.
name|get
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// change edit
name|Optional
argument_list|<
name|ChangeEdit
argument_list|>
name|edit
init|=
name|changeEditUtil
operator|.
name|byChange
argument_list|(
name|notes
argument_list|)
decl_stmt|;
if|if
condition|(
name|edit
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|revision
operator|=
name|ObjectId
operator|.
name|toString
argument_list|(
name|edit
operator|.
name|get
argument_list|()
operator|.
name|getEditCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
else|else
block|{
name|PatchSet
name|patchSet
init|=
name|psUtil
operator|.
name|get
argument_list|(
name|notes
argument_list|,
name|patchKey
operator|.
name|patchSetId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|patchSet
operator|==
literal|null
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
name|revision
operator|=
name|patchSet
operator|.
name|commitId
argument_list|()
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
decl||
name|NoSuchChangeException
decl||
name|AuthException
name|e
parameter_list|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|getServletContext
argument_list|()
operator|.
name|log
argument_list|(
literal|"Cannot query database"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|path
init|=
name|patchKey
operator|.
name|fileName
argument_list|()
decl_stmt|;
name|String
name|restUrl
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%s/changes/%d/revisions/%s/files/%s/download?parent=%d"
argument_list|,
name|req
operator|.
name|getContextPath
argument_list|()
argument_list|,
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
name|revision
argument_list|,
name|Url
operator|.
name|encode
argument_list|(
name|path
argument_list|)
argument_list|,
name|side
argument_list|)
decl_stmt|;
name|rsp
operator|.
name|sendRedirect
argument_list|(
name|restUrl
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

